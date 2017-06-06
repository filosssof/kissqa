package org.fiodorov.view;

import java.time.Instant;

/**
 * @author rfiodorov
 *         on 5/10/17.
 */
public class AnswerView {

    private Long id;

    private String content;

    private Instant createdDate;

    private UserView createdBy;

    private Integer rank;

    private Boolean votedUpByActualUser;

    private Boolean votedDownByActualUser;

    private Boolean best;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getVotedUpByActualUser() {
        return votedUpByActualUser;
    }

    public void setVotedUpByActualUser(Boolean votedUpByActualUser) {
        this.votedUpByActualUser = votedUpByActualUser;
    }

    public Boolean getVotedDownByActualUser() {
        return votedDownByActualUser;
    }

    public void setVotedDownByActualUser(Boolean votedDownByActualUser) {
        this.votedDownByActualUser = votedDownByActualUser;
    }

    public Boolean getBest() {
        return best;
    }

    public void setBest(Boolean best) {
        this.best = best;
    }
}
