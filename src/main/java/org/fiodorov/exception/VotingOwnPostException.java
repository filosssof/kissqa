package org.fiodorov.exception;

import org.fiodorov.app.options.ErrorCode;

/**
 * Created by User-MD037 on 29.02.2016.
 */
public class VotingOwnPostException extends PreconditionFailedException {
    public VotingOwnPostException() {
        super(ErrorCode.VOTE_OWN_QUESTION);
    }

    public VotingOwnPostException(ErrorCode code) {
        super(code);
    }
}
