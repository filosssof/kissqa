package org.fiodorov.validators.impl;

import org.apache.commons.lang3.StringUtils;
import org.fiodorov.app.options.ErrorCode;
import org.fiodorov.validators.AbstractConstraintValidator;
import org.fiodorov.validators.api.NotNullOrBlank;

/**
 * @author rfiodorov
 *         on 2/15/17.
 */

public class NotNullOrBlankValidator extends AbstractConstraintValidator<NotNullOrBlank, String> {

    private ErrorCode code;

    @Override
    public void initialize(NotNullOrBlank constraintAnnotation) {
        code = constraintAnnotation.code();
    }

    @Override
    public boolean isValid(String value) {
        return StringUtils.isNotBlank(value);
    }

    @Override
    protected ErrorCode getErrorCode() {
        return code;
    }
}
