package org.fiodorov.validators.api;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.fiodorov.app.options.ErrorCode;
import org.fiodorov.validators.impl.NotNullOrNegativeValidator;

/**
 * @author rfiodorov
 *         on 2/15/17.
 */
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = NotNullOrNegativeValidator.class)
public @interface NotNullOrNegative {

    ErrorCode code() default ErrorCode.NOT_NULL_OR_NEGATIVE_ID;

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
