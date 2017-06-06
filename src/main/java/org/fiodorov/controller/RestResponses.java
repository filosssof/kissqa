package org.fiodorov.controller;

import java.nio.charset.Charset;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.fiodorov.app.options.ErrorCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

/**
 * @author rfiodorov
 *         on 2/7/17.
 */
public final class RestResponses {

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private final MediaType mediaType;

    private RestResponses(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public static RestResponses json() {
        return new RestResponses(new MediaType("application", "json", DEFAULT_CHARSET));
    }

    public static RestResponses text() {
        return new RestResponses(MediaType.TEXT_PLAIN);
    }

    public static RestResponses html() {
        return new RestResponses(MediaType.TEXT_HTML);
    }

    public <T> ResponseEntity<T> ok(T result) {
        return response(result, HttpStatus.OK);
    }

    public ResponseEntity<Void> ok() {
        return response(null, HttpStatus.OK);
    }

    /**
     * throws a {@link EntityNotFoundException} if this Optional is empty
     *
     */
    public <T> ResponseEntity<T> ok(Optional<T> result) {
        if (result.isPresent()) {
            return response(result.get(), HttpStatus.OK);
        }

        throw new EntityNotFoundException(ErrorCode.SEARCHED_ENTITY_NOT_FOUND.getActualCode());
    }

    public <T> ResponseEntity<T> okWithHeaders(T result, MultiValueMap<String, String> headers) {
        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }

    public <T> ResponseEntity<T> accepted(T result) {
        return response(result, HttpStatus.ACCEPTED);
    }

    public <T> ResponseEntity<T> accepted() {
        return response(null, HttpStatus.ACCEPTED);
    }

    public <T> ResponseEntity<T> createdWithHeaders(T result, MultiValueMap<String, String> headers) {
        return new ResponseEntity<>(result, headers, HttpStatus.CREATED);
    }

    public ResponseEntity<Void> created() {
        return response(null, HttpStatus.CREATED);
    }

    public <T> ResponseEntity<T> created(T t) {
        return response(t, HttpStatus.CREATED);
    }

    public ResponseEntity<Void> updated() {
        return response(null, HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<Void> deleted() {
        return response(null, HttpStatus.NO_CONTENT);
    }

    public <T> ResponseEntity<T> response(T result, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        return new ResponseEntity<>(result, headers, status);
    }

    public <T> ResponseEntity<T> response(ResponseEntity<T> entity) {
        return response(entity.getBody(), entity.getStatusCode());
    }
}
