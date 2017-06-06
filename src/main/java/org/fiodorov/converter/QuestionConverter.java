package org.fiodorov.converter;

import org.fiodorov.model.Question;
import org.fiodorov.model.Vote;
import org.fiodorov.view.QuestionView;

import com.google.common.base.Converter;

/**
 * @author rfiodorov
 *         on 1/30/17.
 */
public class QuestionConverter extends Converter<Question, QuestionView> {

    private UserConverter userConverter = new UserConverter();

    @Override
    protected QuestionView doForward(Question question) {
        QuestionView questionView = new QuestionView();
        questionView.setId(question.getId());
        questionView.setContent(question.getContent());
        questionView.setTitle(question.getTitle());
        questionView.setCreatedDate(question.getCreatedDate());
        questionView.setCreatedBy(userConverter.convert(question.getCreatedBy()));
        questionView.setVotedUpByActualUser(Vote.UP.equals(question.isVotedByCurrent()));
        questionView.setVotedDownByActualUser(Vote.DOWN.equals(question.isVotedByCurrent()));
        questionView.setRank(question.getRank());
        questionView.setAnswered(question.isAnswered());
        return questionView;
    }

    @Override
    protected Question doBackward(QuestionView questionView) {
        Question question = new Question();
        question.setContent(questionView.getContent());
        question.setTitle(questionView.getTitle());
        return question;
    }
}
