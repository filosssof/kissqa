package org.fiodorov.view;

/**
 * @author rfiodorov
 *         on 5/12/17.
 */
public class UserResponseView {

    private UserView user;

    public UserResponseView(UserView user) {
        this.user = user;
    }

    public UserView getUser() {
        return user;
    }

    public void setUser(UserView user) {
        this.user = user;
    }
}
