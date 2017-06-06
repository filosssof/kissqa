package org.fiodorov.exception;

import java.io.IOException;

import org.fiodorov.view.ExceptionResponseView;
import org.springframework.http.HttpStatus;

/**
 * @author rfiodorov
 *         on 2/7/17.
 */
public class RestTemplateException extends IOException {

    private final HttpStatus statusCode;

    private final ExceptionResponseView body;

    public RestTemplateException(HttpStatus statusCode, ExceptionResponseView body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public ExceptionResponseView getBody() {
        return body;
    }
}

