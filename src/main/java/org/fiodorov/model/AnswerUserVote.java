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
 *         on 5/18/17.
 */
@Table(name="answer_user_votes", schema = Defaults.SCHEMA)
@Entity
@NamedEntityGraphs({
        @NamedEntityGraph(name="noJoinsVoteAnswer")
})
public class AnswerUserVote extends UserVote{

    @ManyToOne(targetEntity = Answer.class)
    @JoinColumn(name = "answer_id", referencedColumnName="id", nullable = false)
    private Answer answer;

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public AnswerUserVote() {
    }

    public AnswerUserVote(User user, Vote vote, Answer answer) {
        this.answer = answer;
        this.setVote(vote);
        this.setUser(user);
    }
}
