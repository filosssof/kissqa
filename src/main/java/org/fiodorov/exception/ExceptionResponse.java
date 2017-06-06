package org.fiodorov.exception;

/**
 * @author rfiodorov
 *         on 2/2/17.
 */
public class ExceptionResponse {
    private String[] errorCodes;

    public ExceptionResponse() {

    }

    public ExceptionResponse(String... errorCodes) {
        this.errorCodes = errorCodes;
    }

    public String[] getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(String[] errorCodes) {
        this.errorCodes = errorCodes;
    }

}
