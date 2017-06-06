package org.fiodorov.exception;

import org.fiodorov.app.options.ErrorCode;

/**
 * Created by User-MD037 on 19.02.2016.
 */
public class RepeatBestAnswerException extends PreconditionFailedException{
    public RepeatBestAnswerException() {
        super(ErrorCode.ALREADY_ANSWERED);
    }
}
