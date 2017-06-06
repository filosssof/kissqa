package org.fiodorov.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

import org.fiodorov.app.options.ErrorCode;
import org.fiodorov.app.options.auth.SecurityUtil;
import org.fiodorov.app.options.auth.TokenAuthenticationService;
import org.fiodorov.app.options.auth.UserRole;
import org.fiodorov.app.options.auth.UserToken;
import org.fiodorov.converter.UserConverter;
import org.fiodorov.model.User;
import org.fiodorov.repository.UserRepository;
import org.fiodorov.service.api.LoginServiceApi;
import org.fiodorov.utils.Defaults;
import org.fiodorov.view.UserView;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.InvalidAuthorizationException;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author rfiodorov
 *         on 2/7/17.
 */
public class LoginServiceImpl implements LoginServiceApi{

    private final UserRepository userRepository;

    private final UserConverter userConverter;

    private final TokenAuthenticationService tokenAuthenticationService;

    public LoginServiceImpl(UserRepository userRepository, TokenAuthenticationService tokenAuthenticationService) {
        this.userRepository = userRepository;
        this.userConverter = new UserConverter();
        this.tokenAuthenticationService = tokenAuthenticationService;
    }

    @Override
    public void update(String email, UserView userView, HttpServletResponse response) throws IOException {
        User user = findByEmail(email).get();
        doUpdateClient(userView, user, response);
    }

    private void doUpdateClient(UserView userView, User user, HttpServletResponse response) throws IOException {
        boolean regeneratePassword = !userView.getEmail().equals(user.getEmail());
        setClientFieldsForUpdate(userView, user);
        userRepository.saveAndFlush(user);
        if (regeneratePassword) {
            regenerateAuthenticationToken(user, response);
        }
    }

    private void setClientFieldsForUpdate(UserView source, User destination) {
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
        destination.setEmail(source.getEmail());
    }

    @Override
    public Optional<UserView> getDetails(String email) {
        return Optional.ofNullable(userRepository.findOneByEmail(email))
                .map(userConverter::convert);
    }

    @Override
    @Transactional
    public void signup(UserView userView, HttpServletResponse response) throws IOException {
        User user = userConverter.reverse().convert(userView);
        user.setEnabled(true);
        user.setRank(Defaults.START_KARMA);
        user.setUserRole(UserRole.SIMPLE);
        user = userRepository.save(user);
        regenerateAuthenticationToken(user, response);
    }

    private void regenerateAuthenticationToken(User user, HttpServletResponse response) throws IOException {
        UserToken authenticatedUser = createUserToken(user);
        SecurityUtil.regenerateAuthenticationToken(tokenAuthenticationService, response, authenticatedUser);
    }

    @Override
    public void loginFacebook(String accessToken, HttpServletResponse response) throws IOException {
        try {
            org.springframework.social.facebook.api.User userProfile = getFacebookUser(accessToken);
            User user = userRepository.findOneByFacebookId(userProfile.getId());
            if (user == null) {
                user = userRepository.findOneByEmail(userProfile.getEmail());
                if (user != null) {
                    user.setFacebookId(userProfile.getId());
                    userRepository.saveAndFlush(user);
                    regenerateAuthenticationToken(user, response);
                } else {
                    User newUser = new User();
                    newUser.setFacebookId(userProfile.getId());
                    newUser.setEmail(userProfile.getEmail());
                    newUser.setFirstName(userProfile.getName());
                    newUser.setRank(Defaults.START_KARMA);
                    User savedUser = userRepository.saveAndFlush(newUser);
                    regenerateAuthenticationToken(savedUser, response);
                }
            } else {
                regenerateAuthenticationToken(user, response);
            }
        } catch (InvalidAuthorizationException ex){
            throw new EntityNotFoundException(ErrorCode.FACEBOOK_ACCOUNT_NOT_FOUND.getActualCode());
        }
    }

    private org.springframework.social.facebook.api.User getFacebookUser(String accessToken) {
        Facebook facebook = new FacebookTemplate(accessToken);
        String[] fields = { "id", "email", "name" };
        return facebook.fetchObject("me", org.springframework.social.facebook.api.User.class, fields);
    }


    private UserToken createUserToken(User user) throws UsernameNotFoundException {
        return new UserToken(user.getEmail(), true, true, true, true,
                Arrays.asList(new SimpleGrantedAuthority(UserRole.getAuthority(user))));
    }


    private Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findOneByEmail(email));
    }
}
