package org.fiodorov.view;

import java.io.Serializable;

import com.google.common.base.Preconditions;

/**
 * @author rfiodorov
 *         on 2/7/17.
 */
public class ExceptionResponseView implements Serializable {
    private static final long serialVersionUID = -8493863492377806814L;

    private String[] errorCodes = null;

    public ExceptionResponseView() {

    }

    public ExceptionResponseView(String... errorCodes) {
        Preconditions.checkArgument(errorCodes != null && errorCodes.length != 0,
                "ErrorCodes must be provided");
        this.errorCodes = errorCodes;
    }

    public String[] getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(String[] errorCodes) {
        Preconditions.checkArgument(errorCodes != null && errorCodes.length != 0,
                "ErrorCodes must be provided");
        this.errorCodes = errorCodes;
    }

}

