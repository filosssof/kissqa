package org.fiodorov.exception;

import org.fiodorov.app.options.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author rfiodorov
 *         on 2/7/17.
 */
@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
public class PreconditionFailedException extends RuntimeException {

    /**
     * @param code
     *            the actual code that UI will map to something meaningful
     */
    public PreconditionFailedException(ErrorCode code) {
        super(code.getActualCode());
    }
}
