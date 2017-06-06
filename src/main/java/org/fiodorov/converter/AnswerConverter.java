package org.fiodorov.converter;

import org.fiodorov.model.Answer;
import org.fiodorov.model.Vote;
import org.fiodorov.view.AnswerView;

import com.google.common.base.Converter;

/**
 * @author rfiodorov
 *         on 5/10/17.
 */
public class AnswerConverter extends Converter<Answer, AnswerView> {

    private UserConverter userConverter = new UserConverter();

    @Override
    protected AnswerView doForward(Answer answer) {
        AnswerView answerView = new AnswerView();
        answerView.setId(answer.getId());
        answerView.setContent(answer.getContent());
        answerView.setCreatedDate(answer.getCreatedDate());
        answerView.setCreatedBy(userConverter.doForward(answer.getCreatedBy()));
        answerView.setRank(answer.getRank());
        answerView.setVotedUpByActualUser(Vote.UP.equals(answer.getVotedByCurrent()));
        answerView.setVotedDownByActualUser(Vote.DOWN.equals(answer.getVotedByCurrent()));
        answerView.setBest(answer.isBest());
        return answerView;
    }

    @Override
    protected Answer doBackward(AnswerView answerView) {
        Answer answer = new Answer();
        answer.setContent(answerView.getContent());
        return answer;
    }
}
