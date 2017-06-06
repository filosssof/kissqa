package org.fiodorov.exception;

import org.fiodorov.app.options.ErrorCode;

/**
 * Created by User-MD037 on 01.03.2016.
 */
public class NotEnoughKarmaException extends PreconditionFailedException{
    public NotEnoughKarmaException() {
        super(ErrorCode.NOT_ENOUGH_KARMA);
    }
}
