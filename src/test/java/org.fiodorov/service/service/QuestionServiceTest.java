package org.fiodorov.service.service;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.fiodorov.exception.NotEnoughKarmaException;
import org.fiodorov.exception.RepeatVotingException;
import org.fiodorov.exception.VotingOwnPostException;
import org.fiodorov.model.Question;
import org.fiodorov.model.QuestionUserVote;
import org.fiodorov.model.User;
import org.fiodorov.model.Vote;
import org.fiodorov.repository.QuestionRepository;
import org.fiodorov.service.api.QuestionServiceApi;
import org.fiodorov.service.api.UserServiceApi;
import org.fiodorov.service.config.service.QuestionServiceTestConfig;
import org.fiodorov.utils.Defaults;
import org.fiodorov.view.AddAnswerView;
import org.fiodorov.view.AddQuestionView;
import org.fiodorov.view.AnswerContentView;
import org.fiodorov.view.QuestionView;
import org.fiodorov.view.VoteView;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Testing QuestionService
 * Created by Roman Fiodorov on 10.03.2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { QuestionServiceTestConfig.class})
public class QuestionServiceTest {

    @Autowired
    private QuestionServiceApi toTest;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserServiceApi userServiceApi;

    private String testTitle = "Test title";
    private String testContent = "Test content";

    private Long mockId = 1L;
    private String mockEmail = "email@example.com";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        doNothing().when(userServiceApi).changeUserRank(any(Question.class), eq(Defaults.QUESTION_ADD));
        when(questionRepository.save(any(Question.class))).thenReturn(null);
        when(questionRepository.findOne(mockId)).thenReturn(getMockQuestion());
    }

    private Question getMockQuestion(){
        Question question = new Question();
        question.setId(mockId);
        question.setTitle(testTitle);
        question.setContent(testContent);
        question.setCreatedBy(getMockUser());
        question.setCreatedDate(Instant.now());
        User anotherUser = getMockUser();
        anotherUser.setId(2L);
        question.getUserVotes().add(new QuestionUserVote(anotherUser,Vote.UP,question));
        return question;
    }

    private AddQuestionView getMockQuestionView(){
        QuestionView view = new QuestionView();
        view.setTitle(testTitle);
        view.setContent(testContent);
        AddQuestionView questionView = new AddQuestionView();
        questionView.setQuestion(view);
        return questionView;
    }

    private User getMockUser(){
        User user = new User();
        user.setId(1L);
        user.setRank(30);
        user.setFirstName("Roman");
        user.setLastName("Fiodorov");
        user.setFacebookId("123123132L");
        user.setEmail("email@example.com");
        return user;
    }

    @Test
    public void testAddingQuestion() throws NotEnoughKarmaException {
        User testUser = getMockUser();
        when(userServiceApi.getUserByEmail(mockEmail)).thenReturn(Optional.of(testUser));
        toTest.createNewQuestion(getMockQuestionView(),mockEmail);
    }

    @Test
    public void testAddingQuestionWithLessKarma() throws NotEnoughKarmaException {
        User testUser = getMockUser();
        testUser.setRank(0);
        when(userServiceApi.getUserByEmail(mockEmail)).thenReturn(Optional.of(testUser));
        thrown.expect(NotEnoughKarmaException.class);
        toTest.createNewQuestion(getMockQuestionView(),mockEmail);
    }

    @Test
    public void testGetQuestionByIdNegative(){
        thrown.expect(EntityNotFoundException.class);
        toTest.getQuestionById(-1L, null);
    }

    @Test
    public void testGetQuestionByIdNull(){
        thrown.expect(EntityNotFoundException.class);
        toTest.getQuestionById(null, null);
    }

    @Test
    public void testGetAnswerByQuestionIdNegative(){
        thrown.expect(EntityNotFoundException.class);
        toTest.getAnswerByQuestionId(-1L, null);
    }

    @Test
    public void testGetAnswerByQuestionIdNull(){
        thrown.expect(EntityNotFoundException.class);
        toTest.getAnswerByQuestionId(null, null);
    }

    @Test
    public void testMarkQuestionWithIdNegative() throws RepeatVotingException, VotingOwnPostException {
        thrown.expect(EntityNotFoundException.class);
        VoteView voteView = new VoteView();
        voteView.setEntityId(-1L);
        voteView.setVote(Vote.UP);
        toTest.voteQuestion(voteView,mockEmail);
    }

    @Test
    public void testMarkQuestionWithIdNull() throws RepeatVotingException, VotingOwnPostException {
        thrown.expect(EntityNotFoundException.class);
        VoteView voteView = new VoteView();
        voteView.setEntityId(null);
        voteView.setVote(Vote.UP);
        toTest.voteQuestion(voteView,mockEmail);
    }

    @Ignore
    @Test
    public void testMarkQuestionOwnPost() throws RepeatVotingException, VotingOwnPostException {
        thrown.expect(VotingOwnPostException.class);
        VoteView voteView = new VoteView();
        voteView.setEntityId(mockId);
        voteView.setVote(Vote.UP);
        toTest.voteQuestion(voteView,mockEmail);
    }

    @Ignore
    @Test
    public void testMarkQuestionRepeatVoting() throws RepeatVotingException, VotingOwnPostException {
        User anotherUser = getMockUser();
        Long anotherMockId = 2L;
        anotherUser.setId(anotherMockId);
        thrown.expect(RepeatVotingException.class);
        String anotherMockEmail = "another@example.com";
        when(userServiceApi.getUserByEmail(anotherMockEmail)).thenReturn(Optional.of(anotherUser));
        VoteView voteView = new VoteView();
        voteView.setEntityId(mockId);
        voteView.setVote(Vote.UP);
        toTest.voteQuestion(voteView, anotherMockEmail);
    }

    @Test
    public void testAddAnswerForQuestionWithIdNegative() throws NotEnoughKarmaException {
        when(userServiceApi.getUserByEmail(mockEmail)).thenReturn(Optional.of(getMockUser()));
        thrown.expect(EntityNotFoundException.class);
        AddAnswerView answerView = new AddAnswerView();
        AnswerContentView answerContentView = new AnswerContentView();
        answerContentView.setContent(testContent);
        answerView.setAnswer(answerContentView);
        toTest.addAnswerForQuestion(-1L,answerView,mockEmail);
    }

    @Test
    public void testAddAnswerForQuestionWithIdNull() throws NotEnoughKarmaException {
        when(userServiceApi.getUserByEmail(mockEmail)).thenReturn(Optional.of(getMockUser()));
        thrown.expect(EntityNotFoundException.class);
        AddAnswerView answerView = new AddAnswerView();
        AnswerContentView answerContentView = new AnswerContentView();
        answerContentView.setContent(testContent);
        answerView.setAnswer(answerContentView);
        toTest.addAnswerForQuestion(null,answerView,mockEmail);
    }

    @Test
    public void testAddAnswerForQuestionWithLessKarma() throws NotEnoughKarmaException {
        thrown.expect(NotEnoughKarmaException.class);
        User testUser = getMockUser();
        testUser.setRank(0);
        when(userServiceApi.getUserByEmail(mockEmail)).thenReturn(Optional.of(testUser));
        AddAnswerView answerView = new AddAnswerView();
        AnswerContentView answerContentView = new AnswerContentView();
        answerContentView.setContent(testContent);
        answerView.setAnswer(answerContentView);
        toTest.addAnswerForQuestion(1L,answerView,mockEmail);
    }
}
