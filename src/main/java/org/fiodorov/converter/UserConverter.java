package org.fiodorov.converter;

import org.fiodorov.model.User;
import org.fiodorov.utils.Password;
import org.fiodorov.view.UserView;

import com.google.common.base.Converter;

/**
 * @author rfiodorov
 *         on 2/7/17.
 */
public class UserConverter extends Converter<User, UserView> {

    @Override
    protected UserView doForward(User user) {
        UserView userView = new UserView();
        userView.setId(user.getId());
        userView.setEmail(user.getEmail());
        userView.setFirstName(user.getFirstName());
        userView.setLastName(user.getLastName());
        userView.setDateOfBirth(user.getDateOfBirth());
        userView.setRank(user.getRank());
        return userView;
    }

    @Override
    protected User doBackward(UserView userView) {
        User user = new User();
        user.setEmail(userView.getEmail() != null ? userView.getEmail() : userView.getUsername());
        user.setFirstName(userView.getFirstName());
        user.setLastName(userView.getLastName());
        user.setDateOfBirth(userView.getDateOfBirth());
        user.setRank(userView.getRank());
        user.setPassword(Password.hashPassword(userView.getPassword()));
        return user;
    }
}
