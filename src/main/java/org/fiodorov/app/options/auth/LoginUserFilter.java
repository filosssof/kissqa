package org.fiodorov.app.options.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fiodorov.repository.UserRepository;
import org.fiodorov.service.impl.UserDetailsServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author rfiodorov
 *         on 2/7/17.
 */
public class LoginUserFilter extends AbstractAuthenticationProcessingFilter{


    private final TokenAuthenticationService tokenAuthenticationService;

    private final UserDetailsServiceImpl clientDetailsService;

    private final UserRepository userRepository;

    public LoginUserFilter(String urlMapping, TokenAuthenticationService tokenAuthenticationService,
            UserDetailsServiceImpl clientDetailsService, AuthenticationManager authManager, UserRepository userRepository) {
        super(new AntPathRequestMatcher(urlMapping));
        this.clientDetailsService = clientDetailsService;
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.userRepository = userRepository;
        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        UserToken user = new ObjectMapper().readValue(request.getInputStream(), UserToken.class);
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(user.getUsername(),
                    user.getPassword());
            return SecurityUtil.authenticate(getAuthenticationManager(), loginToken, response);
        } else {
            AuthenticationErrorHandler.badCredential(response);
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain, Authentication authentication) throws IOException, ServletException {
        UserToken authenticatedUser = clientDetailsService.loadUserByUsername(authentication.getName());
        SecurityUtil.authenticateUser(tokenAuthenticationService, response, authenticatedUser);
        super.successfulAuthentication(request, response, chain, authentication);
    }

    @Override
    protected AuthenticationManager getAuthenticationManager() {
        return new AppAuthenticationManager(userRepository);
    }
}
