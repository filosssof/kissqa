package org.fiodorov.validators.impl;

import org.fiodorov.app.options.ErrorCode;
import org.fiodorov.validators.AbstractConstraintValidator;
import org.fiodorov.validators.api.NotNullOrNegative;

/**
 * @author rfiodorov
 *         on 2/15/17.
 */
public class NotNullOrNegativeValidator extends AbstractConstraintValidator<NotNullOrNegative, Long> {

    private ErrorCode code;

    @Override
    public void initialize(NotNullOrNegative constraintAnnotation) {
        code = constraintAnnotation.code();
    }

    @Override
    public boolean isValid(Long value) {
        return value != null && value.longValue() != 0 && value.longValue() >> 63 == 0;
    }

    @Override
    protected ErrorCode getErrorCode() {
        return code;
    }
}
