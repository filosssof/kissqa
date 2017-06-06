package org.fiodorov.model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.fiodorov.utils.Defaults;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * Class model for Question
 * Created by Roman Fiodorov on 16.11.2015.
 */
@Entity
@Table(name = "question", schema = Defaults.SCHEMA)
@NamedEntityGraphs({
        @NamedEntityGraph(name="standard", attributeNodes = {
                @NamedAttributeNode("userVotes"),
                @NamedAttributeNode("answers")
        }),
        @NamedEntityGraph(name="noJoins")
})
public class Question extends GenericModel {

    public Question() {
    }

    public Question(int rank) {
        this.setRank(rank);
    }

    @Column(name = "title", nullable = false)
    private String title;

    @OneToMany(mappedBy = "question")
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<Answer> answers = new LinkedList<>();

    @Column(name = "views", nullable = false, columnDefinition = "int default 0")
    private int views = 0;

    @Formula("(SELECT COUNT(*) FROM app.answer WHERE app.answer.question=id)")
    private int answersNum = 0;

    @Column(name = "is_answered", nullable = false, columnDefinition = "boolean default false")
    private boolean isAnswered;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private Set<QuestionUserVote> userVotes = new HashSet<>();

    @Transient
    private Vote votedByCurrent;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getAnswersNum() {
        return answersNum;
    }

    public void setAnswersNum(int answersNum) {
        this.answersNum = answersNum;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

    public Vote isVotedByCurrent() {
        return votedByCurrent;
    }

    public void setVotedByCurrent(Vote votedByCurrent) {
        this.votedByCurrent = votedByCurrent;
    }

    public Vote getVotedByCurrent() {
        return votedByCurrent;
    }

    public Set<QuestionUserVote> getUserVotes() {
        return userVotes;
    }

    public void setUserVotes(Set<QuestionUserVote> userVotes) {
        this.userVotes = userVotes;
    }
}
