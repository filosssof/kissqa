package org.fiodorov.exception;

import org.springframework.security.authentication.AccountStatusException;

/**
 * @author rfiodorov
 *         on 2/2/17.
 */
public class UserInBlackListException extends AccountStatusException {
    public UserInBlackListException(String msg) {
        super(msg);
    }

    public UserInBlackListException(String msg, Throwable t) {
        super(msg, t);
    }
}
