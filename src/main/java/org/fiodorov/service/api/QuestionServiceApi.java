package org.fiodorov.service.api;

import java.security.Principal;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.fiodorov.exception.NotEnoughKarmaException;
import org.fiodorov.exception.RepeatVotingException;
import org.fiodorov.exception.VotingOwnPostException;
import org.fiodorov.view.AddAnswerView;
import org.fiodorov.view.AddQuestionView;
import org.fiodorov.view.AnswerListView;
import org.fiodorov.view.QuestionResponseView;
import org.fiodorov.view.QuestionView;
import org.fiodorov.view.VoteView;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author rfiodorov
 *         on 1/30/17.
 */
public interface QuestionServiceApi {

    List<QuestionView> getAllQuestions(Integer set, Principal principal);

    QuestionResponseView getQuestionById(Long questionId, Principal principal) throws EntityNotFoundException;

    @Transactional(readOnly = true) AnswerListView getAnswerByQuestionId(Long questionId, Principal principal) throws EntityNotFoundException;

    void createNewQuestion(AddQuestionView questionView, String userEmail) throws NotEnoughKarmaException;

    void voteQuestion(VoteView voteView, String userEmail) throws EntityNotFoundException, RepeatVotingException,
            VotingOwnPostException;

    void addAnswerForQuestion(Long questionId, AddAnswerView addAnswerView, String userEmail) throws NotEnoughKarmaException;

    Integer getQuestionRank(Long questionId);
}
