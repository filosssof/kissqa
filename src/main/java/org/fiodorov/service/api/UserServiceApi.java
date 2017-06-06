package org.fiodorov.service.api;

import java.security.Principal;
import java.util.Optional;

import org.fiodorov.model.GenericModel;
import org.fiodorov.model.User;
import org.fiodorov.view.UserResponseView;

/**
 * @author rfiodorov
 *         on 1/30/17.
 */
public interface UserServiceApi {

    void changeUserRank(GenericModel model, int value);

    Optional<User> getUserByEmail(String email);

    UserResponseView getActualUserResponseView(Principal principal);
}
