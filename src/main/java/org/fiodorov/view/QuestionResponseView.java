package org.fiodorov.view;

/**
 * @author rfiodorov
 *         on 5/12/17.
 */
public class QuestionResponseView {

    private QuestionView question;

    public QuestionResponseView(QuestionView question) {
        this.question = question;
    }

    public QuestionView getQuestion() {
        return question;
    }

    public void setQuestion(QuestionView question) {
        this.question = question;
    }
}
