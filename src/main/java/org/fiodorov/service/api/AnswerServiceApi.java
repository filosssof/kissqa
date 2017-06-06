package org.fiodorov.service.api;

import javax.persistence.EntityNotFoundException;

import org.fiodorov.exception.NoRightsException;
import org.fiodorov.exception.NotLoggedInException;
import org.fiodorov.exception.RepeatBestAnswerException;
import org.fiodorov.view.VoteView;

/**
 * @author rfiodorov
 *         on 1/30/17.
 */
public interface AnswerServiceApi {

    void voteAnswer(VoteView voteView, String userEmail);

    void bestAnswer(Long answerId,String userEmail) throws EntityNotFoundException, NotLoggedInException, NoRightsException, RepeatBestAnswerException;

    Integer getAnswerRank(Long id);
}
