package org.fiodorov.validators;

import java.lang.annotation.Annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.fiodorov.app.options.ErrorCode;

/**
 * @author rfiodorov
 *         on 2/15/17.
 */
public abstract class AbstractConstraintValidator<A extends Annotation, T>
        implements ConstraintValidator<A, T> {

    @Override
    public boolean isValid(T value, ConstraintValidatorContext context) {
        boolean isValid = isValid(value);
        if (!isValid) {
            context.buildConstraintViolationWithTemplate(getErrorCode().getActualCode())
                    .addConstraintViolation();
            context.disableDefaultConstraintViolation();
        }
        return isValid;
    }


    protected abstract boolean isValid(T value);

    protected abstract ErrorCode getErrorCode();

}
