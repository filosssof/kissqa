package org.fiodorov.exception;

import org.fiodorov.app.options.ErrorCode;

/**
 * Exception used when user try to vote again
 * Created by Roman Fiodorov on 10.12.2015.
 */
public class RepeatVotingException extends PreconditionFailedException {

    public RepeatVotingException() {
        super(ErrorCode.REPEAT_VOTING_EXCEPTION);
    }
}
