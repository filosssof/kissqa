package org.fiodorov.validators.impl;

import java.util.Optional;

import org.fiodorov.app.options.ErrorCode;
import org.fiodorov.validators.AbstractConstraintValidator;
import org.fiodorov.validators.api.FacebookTokenValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.social.InvalidAuthorizationException;
import org.springframework.social.RevokedAuthorizationException;
import org.springframework.social.UncategorizedApiException;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.UserIdForApp;
import org.springframework.social.facebook.api.impl.FacebookTemplate;

/**
 * @author rfiodorov
 *         on 5/11/17.
 */
public class FacebookTokenValidatorImpl extends AbstractConstraintValidator<FacebookTokenValidator, String> {

    private ErrorCode code;

    @Autowired
    Environment env;

    public final Logger LOGGER = LoggerFactory.getLogger(FacebookTokenValidatorImpl.class);

    @Override
    protected boolean isValid(String value) {
        try {
            String appKey = env.getProperty("facebook.appKey");
            Facebook facebook = new FacebookTemplate(value);
            Optional<UserIdForApp> optional = facebook.userOperations().getIdsForBusiness().stream().filter(app -> app.getApp().getId().equals(appKey)).findFirst();
            return optional.isPresent();
        } catch (InvalidAuthorizationException | UncategorizedApiException | RevokedAuthorizationException ex) {
            LOGGER.error(ex.getLocalizedMessage(), ex);
            code = ErrorCode.INVALID_FACEBOOK_TOKEN;
            return false;
        }
    }

    @Override
    protected ErrorCode getErrorCode() {
        return code;
    }

    @Override
    public void initialize(FacebookTokenValidator facebookTokenValidator) {
        code = facebookTokenValidator.code();
    }
}

