package org.fiodorov.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.fiodorov.service.api.LoginServiceApi;
import org.fiodorov.service.api.UserServiceApi;
import org.fiodorov.view.LoginFacebookView;
import org.fiodorov.view.UserResponseView;
import org.fiodorov.view.UserView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rfiodorov
 *         on 2/7/17.
 */
@RestController
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    private final RestResponses restResponses = RestResponses.json();

    private final LoginServiceApi loginService;
    private final UserServiceApi userServiceApi;

    public LoginController(LoginServiceApi loginService, UserServiceApi userServiceApi) {
        this.loginService = Objects.requireNonNull(loginService);
        this.userServiceApi = Objects.requireNonNull(userServiceApi);
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> loginClient() {
        return restResponses.ok();
    }

    @RequestMapping(value = "/facebook/login", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> loginFacebookClient(@RequestBody
            LoginFacebookView facebookView, HttpServletResponse response) throws IOException {
        loginService.loginFacebook(facebookView.getToken(), response);
        return restResponses.ok();
    }

    @RequestMapping(value = "/user/signup", method = RequestMethod.POST, produces = "application/json")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Void> signup(@RequestBody
            UserView userView,HttpServletResponse response) throws IOException {
        LOGGER.info("Called signup({}) service", userView.getEmail());
        loginService.signup(userView, response);
        return restResponses.created();
    }

    @RequestMapping("/user")
    public UserResponseView user(Principal principal) {
        return userServiceApi.getActualUserResponseView(principal);
    }

}
