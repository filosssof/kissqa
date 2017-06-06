package org.fiodorov.view;

import java.util.List;

/**
 * @author rfiodorov
 *         on 5/12/17.
 */
public class AnswerListView {
    private List<AnswerView> answers;

    public AnswerListView(List<AnswerView> answers) {
        this.answers = answers;
    }

    public List<AnswerView> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerView> answers) {
        this.answers = answers;
    }
}
