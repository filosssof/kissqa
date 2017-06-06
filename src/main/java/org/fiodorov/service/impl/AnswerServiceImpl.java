package org.fiodorov.service.impl;

import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.fiodorov.app.options.ErrorCode;
import org.fiodorov.exception.NoRightsException;
import org.fiodorov.exception.NotLoggedInException;
import org.fiodorov.exception.RepeatBestAnswerException;
import org.fiodorov.exception.RepeatVotingException;
import org.fiodorov.exception.VotingOwnPostException;
import org.fiodorov.model.Answer;
import org.fiodorov.model.AnswerUserVote;
import org.fiodorov.model.Question;
import org.fiodorov.model.User;
import org.fiodorov.model.Vote;
import org.fiodorov.repository.AnswerRepository;
import org.fiodorov.repository.AnswerVoteRepository;
import org.fiodorov.repository.QuestionRepository;
import org.fiodorov.service.api.AnswerServiceApi;
import org.fiodorov.service.api.UserServiceApi;
import org.fiodorov.utils.Defaults;
import org.fiodorov.view.VoteView;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for handling answer queries
 * Created by Roman Fiodorov on 11.12.2015.
 */
public class AnswerServiceImpl implements AnswerServiceApi{

    private final AnswerRepository answerRepository;

    private final QuestionRepository questionRepository;

    private final UserServiceApi appUserService;

    private final AnswerVoteRepository answerVoteRepository;


    public AnswerServiceImpl(AnswerRepository answerRepository, QuestionRepository questionRepository, UserServiceApi appUserService,
            AnswerVoteRepository answerVoteRepository) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.appUserService = appUserService;
        this.answerVoteRepository = answerVoteRepository;
    }

    @Transactional
    public void voteAnswer(VoteView voteView, String userEmail) {
        Optional<User> actionUser = appUserService.getUserByEmail(userEmail);
        Long answerId = voteView.getEntityId();
        if (answerId==null || answerId<=0){
            throw new EntityNotFoundException("The answer id cannot be null or <= 0");
        }
        Answer answer = answerRepository.findOne(answerId);
        if (answer==null){
            throw new EntityNotFoundException("The answer cannot be found by this id");
        }

        if(actionUser.isPresent()) {
            Optional<AnswerUserVote> dbUserVote = answerVoteRepository.findByAnswerIdAndUserId(answerId,actionUser.get().getId());

            if (dbUserVote.isPresent()) {
                throw new RepeatVotingException();
            }

            if (Objects.equals(answer.getCreatedBy().getId(), actionUser.get().getId())) {
                throw new VotingOwnPostException(ErrorCode.VOTE_OWN_ANSWER);
            }

            Integer currentRank = answer.getRank();
            if (Vote.UP.equals(voteView.getVote())) {
                answer.setRank(++currentRank);
                appUserService.changeUserRank(answer, Defaults.ANSWER_MARK_UP);
            } else {
                answer.setRank(--currentRank);
                appUserService.changeUserRank(answer, Defaults.ANSWER_MARK_DOWN);
            }
            AnswerUserVote answerUserVote = new AnswerUserVote(actionUser.get(),voteView.getVote(),answer);
            answer.getUserVotes().add(answerUserVote);
            answerRepository.save(answer);
        }
    }

    @Transactional
    public void bestAnswer(Long answerId,String userEmail) throws EntityNotFoundException, NotLoggedInException, NoRightsException, RepeatBestAnswerException {
        Optional<User> user = appUserService.getUserByEmail(userEmail);

        if(user.isPresent()) {
            if (answerId == null || answerId <= 0) {
                throw new EntityNotFoundException("The answer cannot be found by this id");
            }

            Answer answer = answerRepository.findOne(answerId);
            if (answer == null) {
                throw new EntityNotFoundException("The answer cannot be found by this id");
            }

            Question question = answer.getQuestion();
            if (!question.getCreatedBy().equals(user.get())) {
                throw new NoRightsException();
            }

            if (question.isAnswered()) {
                throw new RepeatBestAnswerException();
            }

            answer.setBest(true);
            answerRepository.save(answer);
            question.setAnswered(true);
            questionRepository.save(question);
            if (!Objects.equals(question.getCreatedBy().getId(), answer.getCreatedBy().getId())) {
                appUserService.changeUserRank(answer, Defaults.BEST_ANSWER);
            }
        }
    }

    @Override
    public Integer getAnswerRank(Long answerId) {
        if(answerId == null || answerId <= 0 ){
            throw new IllegalArgumentException("The question id was not found");
        }
        return answerRepository.loadRankForAnswer(answerId);
    }
}
