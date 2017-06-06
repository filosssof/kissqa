package org.fiodorov.app.options.auth;

import org.fiodorov.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @author rfiodorov
 *         on 2/2/17.
 */
public enum UserRole {
    ANONYMOUS("ROLE_ANONYMOUS"),SIMPLE("ROLE_SIMPLE_USER"), ADMIN("ROLE_ADMIN"),  FACEBOOK_CLIENT("FACEBOOK_CLIENT");

    private final String authority;
    UserRole(String authority) {
        this.authority = authority;
    }

    public SimpleGrantedAuthority asAuthorityFor() {
        return new SimpleGrantedAuthority(getAuthority());
    }

    public static UserRole valueOf(final GrantedAuthority authority) {
        for (UserRole userRole : values()) {
            if (userRole.getAuthority().equals(authority.getAuthority())) {
                return userRole;
            }
        }
        throw new IllegalArgumentException("No role defined for authority: " + authority.getAuthority());
    }

    public String getAuthority() {
        return authority;
    }

    public static String getAuthority(User user) {
        return user.getFacebookId() == null ? SIMPLE.authority : FACEBOOK_CLIENT.authority;
    }

}
