package org.fiodorov.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.fiodorov.utils.Defaults;

/**
 * Class model for Answer
 * Created by Roman Fiodorov on 16.11.2015.
 */
@Entity
@Table(name="answer", schema= Defaults.SCHEMA)
public class Answer extends GenericModel {

    @ManyToOne(targetEntity = Question.class)
    @JoinColumn(name="question", referencedColumnName="id")
    private Question question;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL)
    private Set<AnswerUserVote> userVotes = new HashSet<>();

    @Column(name = "is_best",nullable = false,  columnDefinition = "boolean default false")
    private boolean isBest = false;

    @Transient
    private Vote votedByCurrent;

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public boolean isBest() {
        return isBest;
    }

    public void setBest(boolean best) {
        isBest = best;
    }

    public Set<AnswerUserVote> getUserVotes() {
        return userVotes;
    }

    public void setUserVotes(Set<AnswerUserVote> userVotes) {
        this.userVotes = userVotes;
    }

    public Vote getVotedByCurrent() {
        return votedByCurrent;
    }

    public void setVotedByCurrent(Vote votedByCurrent) {
        this.votedByCurrent = votedByCurrent;
    }
}
