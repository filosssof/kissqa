package org.fiodorov.app.options;

/**
 * @author rfiodorov
 *         on 2/2/17.
 */
public enum ErrorCode {

    GENERIC_EXCEPTION_IS_HANDLED("GENERIC Exception"),

    BAD_CREDENTIAL("Bad credentials"),

    LOCKED_USER("User is locked"),

    DISABLED_USER("User is Disabled"),
    NOT_NULL_OR_NEGATIVE_ID("Id should be neither null nor negative"),
    NOT_NULL_OR_BLANK("This field cannot be blank"),
    FIRST_NAME_NOT_NULL_OR_BLANK("First name cannot be blank"),
    LAST_NAME_NOT_NULL_OR_BLANK("Last name cannot be blank"),
    EMAIL_NOT_NULL_OR_BLANK("Email cannot be blank"),
    PASSWORD_NOT_NULL_OR_BLANK("Password cannot be null or bank"),

    NOT_ENOUGH_KARMA("Not enough karma for this"),
    REPEAT_VOTING_EXCEPTION("You are voted yet"),
    VOTE_OWN_QUESTION("You cannot vote your own question"),
    VOTE_OWN_ANSWER("You cannot vote your own answer"),
    SELECT_BEST_ANSWER("You should be the author of the question"),
    ALREADY_ANSWERED("THis question is already answered"),

    GENERIC_AUTHENTICATION_EXCEPTION("Generic authentication exception"),

    FACEBOOK_ACCOUNT_EXCEPTION("Facebook account exception"),

    BLACK_LIST_USER("User is in black list"),
    INVALID_TOKEN("Invalid token"),

    EXPIRED_TOKEN("Expired tokens"),
    DIFFERENT_TOKEN("Different token"),
    REQUEST_UNAUTHORIZED("Request unauthorized"),

    REQUEST_UNAUTHORIZED_FOR_FACEBOOK_CLIENT("Request unauthorized for facebook client"),

    FACEBOOK_ACCOUNT_NOT_FOUND("FB not found"),

    SEARCHED_ENTITY_NOT_FOUND("Entity not found"),

    FACEBOOK_ID_ALREADY_EXIST("FB already exists"),

    DELETED_USER("User was deleted"), INVALID_FACEBOOK_TOKEN("Facebook token is invalid");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    /**
     * @return the actual String that makes sense for UI
     */
    public String getActualCode() {
        return code;
    }
}
