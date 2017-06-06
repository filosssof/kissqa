package org.fiodorov.app.options.auth;

import org.fiodorov.model.User;
import org.fiodorov.repository.UserRepository;
import org.fiodorov.utils.Password;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * @author rfiodorov
 *         on 5/8/17.
 */
@Component
public class AppAuthenticationManager implements AuthenticationManager {

    private UserRepository userRepository;

    public AppAuthenticationManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getPrincipal() + "";
        String password = authentication.getCredentials() + "";

        User user = userRepository.findOneByEmail(username);
        if (user == null) {
            throw new BadCredentialsException("1000");
        }

        if(user.getPassword() == null){
            throw new BadCredentialsException("1000");
        }
        if (!user.isEnabled()) {
            throw new DisabledException("1001");
        }
        if (!Password.checkPassword(password, user.getPassword().trim())) {
            throw new BadCredentialsException("1000");
        }
        return new UsernamePasswordAuthenticationToken(username, password);
    }
}
