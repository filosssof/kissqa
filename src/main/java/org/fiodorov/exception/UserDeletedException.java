package org.fiodorov.exception;

/**
 * @author rfiodorov
 *         on 2/2/17.
 */
public class UserDeletedException extends RuntimeException{
    public UserDeletedException(String msg) {
        super(msg);
    }

    public UserDeletedException(String msg, Throwable t) {
        super(msg, t);
    }
}
