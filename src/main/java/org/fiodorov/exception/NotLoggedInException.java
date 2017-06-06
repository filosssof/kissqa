package org.fiodorov.exception;

/**
 * Created by User-MD037 on 17.02.2016.
 */
public class NotLoggedInException extends Exception {
    public NotLoggedInException() {
    }

    public NotLoggedInException(String message) {
        super(message);
    }
}