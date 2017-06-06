package org.fiodorov.service.impl;

import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.fiodorov.converter.AnswerConverter;
import org.fiodorov.converter.QuestionConverter;
import org.fiodorov.exception.NotEnoughKarmaException;
import org.fiodorov.exception.RepeatVotingException;
import org.fiodorov.exception.VotingOwnPostException;
import org.fiodorov.model.Answer;
import org.fiodorov.model.AnswerUserVote;
import org.fiodorov.model.Question;
import org.fiodorov.model.QuestionUserVote;
import org.fiodorov.model.User;
import org.fiodorov.model.Vote;
import org.fiodorov.repository.AnswerRepository;
import org.fiodorov.repository.AnswerVoteRepository;
import org.fiodorov.repository.QuestionRepository;
import org.fiodorov.repository.QuestionUserVoteRepository;
import org.fiodorov.service.api.QuestionServiceApi;
import org.fiodorov.service.api.UserServiceApi;
import org.fiodorov.utils.Defaults;
import org.fiodorov.view.AddAnswerView;
import org.fiodorov.view.AddQuestionView;
import org.fiodorov.view.AnswerListView;
import org.fiodorov.view.AnswerView;
import org.fiodorov.view.QuestionResponseView;
import org.fiodorov.view.QuestionView;
import org.fiodorov.view.VoteView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Roman Fiodorov
 * on 17.11.2015.
 */
public class QuestionServiceImpl implements QuestionServiceApi{

    private final QuestionRepository questionRepository;

    private final UserServiceApi appUserService;

    private final AnswerRepository answerRepository;

    private final QuestionUserVoteRepository questionUserVoteRepository;

    private final AnswerVoteRepository answerVoteRepository;

    private final AnswerConverter answerConverter;

    private QuestionConverter questionConverter;

    public QuestionServiceImpl(QuestionRepository questionRepository, UserServiceApi appUserService, AnswerRepository answerRepository,
            QuestionUserVoteRepository questionUserVoteRepository, AnswerVoteRepository answerVoteRepository) {
        this.questionRepository = questionRepository;
        this.appUserService = appUserService;
        this.answerRepository = answerRepository;
        this.questionUserVoteRepository = questionUserVoteRepository;
        this.answerVoteRepository = answerVoteRepository;
        this.questionConverter = new QuestionConverter();
        this.answerConverter = new AnswerConverter();
    }

    @Transactional(readOnly = true)
    @Override
    public List<QuestionView> getAllQuestions(Integer set, Principal principal) {
        int numPerPage = 10;
        final PageRequest page = new PageRequest(
                set, numPerPage, new Sort(
                new Sort.Order(Sort.Direction.DESC, "createdDate")
        )
        );
        Page<Question> responsePage = questionRepository.findAll(page);
        List<Question> questions = responsePage.getContent();
        return questions.stream().map(question -> questionConverter.convert(question)).collect(Collectors.toList());
    }


    public QuestionResponseView getQuestionById(Long questionId, Principal principal) throws EntityNotFoundException {
        if (questionId == null || questionId < 1) {
            throw new EntityNotFoundException("The question id cannot be null or <= 0");
        }

        QuestionView questionView = questionConverter.convert(questionRepository.findOne(questionId));

        if(principal!=null){
            Optional<User> currentUser = appUserService.getUserByEmail(principal.getName());
            if(currentUser.isPresent()){
                Optional<QuestionUserVote> userVote = questionUserVoteRepository.findByQuestionIdAndUserId(questionId, currentUser.get().getId());
                if(userVote.isPresent()){
                    questionView.setVotedDownByActualUser(Vote.DOWN.equals(userVote.get().getVote()));
                    questionView.setVotedUpByActualUser(Vote.UP.equals(userVote.get().getVote()));
                }
            }
        }

        return new QuestionResponseView(questionView);
    }

    @Override
    @Transactional(readOnly = true)
    public AnswerListView getAnswerByQuestionId(Long questionId, Principal principal) throws EntityNotFoundException {

        if (questionId == null || questionId <= 0) {
            throw new EntityNotFoundException("The question id cannot be null or <= 0");
        }

        List<Answer> answers = answerRepository.findByQuestionIdOrderByIsBestDescRankDescCreatedDateDesc(questionId);
        List<AnswerView> answerViews = answers.stream().map(answerConverter::convert).collect(Collectors.toList());
        if (principal != null) {
            Optional<User> currentUser = appUserService.getUserByEmail(principal.getName());
            currentUser.ifPresent(user -> answerViews.forEach(answerView -> {
                Optional<AnswerUserVote> userVote = answerVoteRepository.findByAnswerIdAndUserId(answerView.getId(), user.getId());
                if (userVote.isPresent()) {
                    answerView.setVotedDownByActualUser(Vote.DOWN.equals(userVote.get().getVote()));
                    answerView.setVotedUpByActualUser(Vote.UP.equals(userVote.get().getVote()));
                }
            }));
        }
        return new AnswerListView(answerViews);
    }

    @Transactional
    public void createNewQuestion(AddQuestionView questionView, String userEmail) throws NotEnoughKarmaException {
        Optional<User> user = appUserService.getUserByEmail(userEmail);
        if(user.isPresent()) {
            if (user.get().getRank() < Math.abs(Defaults.QUESTION_ADD)) {
                throw new NotEnoughKarmaException();
            }
            Question newQuestion = questionConverter.reverse().convert(questionView.getQuestion());
            newQuestion.setCreatedBy(user.get());
            newQuestion.setCreatedDate(Instant.now());
            questionRepository.save(newQuestion);
            appUserService.changeUserRank(newQuestion, Defaults.QUESTION_ADD);
        }
    }

    @Transactional
    public void voteQuestion(VoteView voteView, String userEmail) throws EntityNotFoundException, RepeatVotingException, VotingOwnPostException {

        Long questionId = voteView.getEntityId();
        Vote vote = voteView.getVote();
        if (questionId == null || questionId <= 0) {
            throw new EntityNotFoundException("The question id cannot be null or <= 0");
        }
        Question question = questionRepository.getOne(questionId);
        if (question == null) {
            throw new EntityNotFoundException("The question cannot be found by this id");
        }

        Optional<User> user = appUserService.getUserByEmail(userEmail);

        if(user.isPresent()) {
            if (Objects.equals(question.getCreatedBy().getId(), user.get().getId())) {
                throw new VotingOwnPostException();
            }

            Optional<QuestionUserVote> dbUserVote = questionUserVoteRepository.findByQuestionIdAndUserId(questionId,user.get().getId());

            if (dbUserVote.isPresent()) {
                throw new RepeatVotingException();
            }

            Integer currentRank = question.getRank();
            if (Vote.UP.equals(vote)) {
                question.setRank(++currentRank);
                appUserService.changeUserRank(question, Defaults.QUESTION_MARK_UP);
            } else {
                question.setRank(--currentRank);
                appUserService.changeUserRank(question, Defaults.QUESTION_MARK_DOWN);
            }
            QuestionUserVote userVote = new QuestionUserVote(user.get(), vote, question);
            question.getUserVotes().add(userVote);
            questionRepository.save(question);
        }
    }

    @Transactional
    public void addAnswerForQuestion(Long questionId, AddAnswerView addAnswerView, String userEmail) throws NotEnoughKarmaException {

        Optional<User> user = appUserService.getUserByEmail(userEmail);

        if(user.isPresent()) {

            if (user.get().getRank() < Math.abs(Defaults.ANSWER_ADD)) {
                throw new NotEnoughKarmaException();
            }

            if (questionId == null || questionId <= 0) {
                throw new EntityNotFoundException("The question id cannot be null or <= 0");
            }
            String content = addAnswerView.getAnswer().getContent();
            if (content != null && !content.isEmpty()) {
                Question question = questionRepository.findOne(questionId);
                if (question != null) {
                    List<Answer> answers = question.getAnswers();
                    Answer answer = new Answer();
                    answer.setContent(content);
                    answer.setCreatedBy(user.get());
                    answer.setCreatedDate(Instant.now());
                    answer.setQuestion(question);
                    answerRepository.save(answer);
                    answers.add(answer);
                    questionRepository.save(question);
                    appUserService.changeUserRank(answer, Defaults.ANSWER_ADD);
                }
            }
        }
    }

    @Override
    public Integer getQuestionRank(Long questionId) {
        if(questionId == null || questionId <= 0 ){
            throw new IllegalArgumentException("The question id was not found");
        }
        return questionRepository.loadRankForQuestion(questionId);
    }
}
