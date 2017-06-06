package org.fiodorov.service.api;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.fiodorov.view.UserView;
import org.springframework.validation.annotation.Validated;

/**
 * @author rfiodorov
 *         on 2/7/17.
 */
@Validated
public interface LoginServiceApi {

    void update(String email, UserView userView, HttpServletResponse response) throws IOException;

    Optional<UserView> getDetails(String email);

    void signup(@Valid UserView userView, HttpServletResponse response) throws IOException;

    void loginFacebook(String accessToken, HttpServletResponse response) throws IOException;
}
