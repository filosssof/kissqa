package org.fiodorov.app.options;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.fiodorov.app.options.auth.UserAuthentication;
import org.fiodorov.app.options.auth.UserRole;
import org.fiodorov.exception.PreconditionFailedException;
import org.fiodorov.exception.RestTemplateException;
import org.fiodorov.exception.UnauthorizedException;
import org.fiodorov.view.ExceptionResponseView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.ResourceAccessException;

/**
 * @author rfiodorov
 *         on 2/7/17.
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    private static final String GENERIC_ERROR = "Generic error";

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public ResponseEntity<ExceptionResponseView> handleValidationException(
            ConstraintViolationException constraintException) {

        List<String> list = constraintException.getConstraintViolations().stream()
                .peek(c -> {
                    LOGGER.debug("{}: {}", c.getMessage(), c.getPropertyPath());
                })
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        ExceptionResponseView view = new ExceptionResponseView(list.toArray(new String[list.size()]));
        return new ResponseEntity<>(view, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public ResponseEntity<ExceptionResponseView> handleValidationException(
            PreconditionFailedException constraintException) {
        LOGGER.debug("errors: {}", constraintException.getMessage());
        ExceptionResponseView view = new ExceptionResponseView(constraintException.getMessage());
        return new ResponseEntity<>(view, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<ExceptionResponseView> handleNotFoundException(EntityNotFoundException e) {
        LOGGER.error(GENERIC_ERROR, e);
        return new ResponseEntity<>(new ExceptionResponseView(e.getLocalizedMessage()), HttpStatus.NOT_FOUND);

    }


    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<ExceptionResponseView> handleAccessDeniedException(AccessDeniedException e) {
        LOGGER.debug(GENERIC_ERROR, e);
        UserAuthentication authentication = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
        ErrorCode code = authentication.getDetails().hasRole(UserRole.FACEBOOK_CLIENT) ?
                ErrorCode.REQUEST_UNAUTHORIZED_FOR_FACEBOOK_CLIENT : ErrorCode.REQUEST_UNAUTHORIZED;
        return new ResponseEntity<>(new ExceptionResponseView(code.getActualCode()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseEntity<ExceptionResponseView> handleUnauthorizedException(UnauthorizedException e) {
        LOGGER.debug(GENERIC_ERROR, e);
        return new ResponseEntity<>(new ExceptionResponseView(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponseView> handleException(Exception e) {
        LOGGER.error(GENERIC_ERROR, e);
        return new ResponseEntity<>(new ExceptionResponseView(ErrorCode.GENERIC_EXCEPTION_IS_HANDLED.getActualCode()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceAccessException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponseView> handleRestTemplateException(ResourceAccessException e) {
        LOGGER.error("Resource Access Exception:", e);
        if (e.getCause() instanceof RestTemplateException) {
            RestTemplateException re = (RestTemplateException) e.getCause();
            return new ResponseEntity<>(re.getBody(), re.getStatusCode());
        } else {
            return handleException(e);
        }
    }

}
