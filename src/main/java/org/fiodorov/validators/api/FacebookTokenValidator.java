package org.fiodorov.validators.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.fiodorov.app.options.ErrorCode;
import org.fiodorov.validators.impl.FacebookTokenValidatorImpl;

/**
 * @author rfiodorov
 *         on 5/11/17.
 */
@Documented
@Constraint(validatedBy = FacebookTokenValidatorImpl.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface FacebookTokenValidator {

    String message() default "";

    ErrorCode code();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

