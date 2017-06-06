package org.fiodorov.view;

import org.fiodorov.model.Vote;

/**
 * This view is used for transport vote for the question
 * @author rfiodorov
 *         on 5/10/17.
 */
public class VoteView {

    private Long entityId;

    private Vote vote;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }
}
