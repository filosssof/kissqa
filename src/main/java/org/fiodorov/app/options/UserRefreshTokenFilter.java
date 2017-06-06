package org.fiodorov.app.options;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fiodorov.app.options.auth.UserAuthentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author rfiodorov
 *         on 2/7/17.
 */
public class UserRefreshTokenFilter extends AbstractAuthenticationProcessingFilter {

    private final TokenRefreshService tokenRefreshService;

    private final UserDetailsService clientDetailsService;

    public UserRefreshTokenFilter(String urlMapping, TokenRefreshService tokenRefreshService,
            AuthenticationManager authManager, UserDetailsService clientDetailsService) {
        super(new AntPathRequestMatcher(urlMapping));
        this.tokenRefreshService = tokenRefreshService;
        this.clientDetailsService = clientDetailsService;
        setAuthenticationManager(authManager);

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        return tokenRefreshService.getAuthentication(request, response);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain, Authentication authentication) throws IOException, ServletException {
        UserAuthentication userAuthentication = tokenRefreshService.getAuthentication(request, response);
        tokenRefreshService.addAuthentication(request, response, userAuthentication);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
    }

}
