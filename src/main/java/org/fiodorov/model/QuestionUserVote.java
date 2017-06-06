package org.fiodorov.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.Table;

import org.fiodorov.utils.Defaults;

/**
 * @author rfiodorov
 *         on 5/17/17.
 */
@Table(name="question_user_votes", schema = Defaults.SCHEMA)
@Entity
@NamedEntityGraphs({
        @NamedEntityGraph(name="noJoinsVoteQuestion")
})
public class QuestionUserVote extends UserVote{

    @ManyToOne(targetEntity = Question.class)
    @JoinColumn(name = "question_id", referencedColumnName="id", nullable = false)
    private Question question;


    public QuestionUserVote() {
    }

    public QuestionUserVote(User user, Vote vote, Question question) {
        this.setVote(vote);
        this.setUser(user);
        this.setQuestion(question);
    }


    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
