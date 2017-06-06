package org.fiodorov.view;

import java.time.Instant;
import java.util.Objects;

/**
 * Created by rfiodorov on 6/19/16.
 */
public class QuestionView {

    private Long id;

    private String title;

    private String content;

    private Instant createdDate;

    private UserView createdBy;

    private boolean votedUpByActualUser;

    private boolean votedDownByActualUser;

    private Integer rank;

    private Boolean isAnswered;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public UserView getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserView createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public boolean isVotedUpByActualUser() {
        return votedUpByActualUser;
    }

    public void setVotedUpByActualUser(boolean votedUpByActualUser) {
        this.votedUpByActualUser = votedUpByActualUser;
    }

    public boolean isVotedDownByActualUser() {
        return votedDownByActualUser;
    }

    public void setVotedDownByActualUser(boolean votedDownByActualUser) {
        this.votedDownByActualUser = votedDownByActualUser;
    }

    public Boolean getAnswered() {
        return isAnswered;
    }

    public void setAnswered(Boolean answered) {
        isAnswered = answered;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        QuestionView that = (QuestionView) o;
        return Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getContent(), that.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getContent());
    }

    @Override
    public String toString() {
        return "QuestionView{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
