package org.fiodorov.app.options.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

/**
 * @author rfiodorov
 *         on 2/2/17.
 */
public class AuthenticationFilter extends GenericFilterBean{

    private final TokenAuthenticationService tokenAuthenticationService;

    public AuthenticationFilter(TokenAuthenticationService taService) {
        this.tokenAuthenticationService = taService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        SecurityContextHolder.getContext()
                .setAuthentication(tokenAuthenticationService.getAuthentication((HttpServletRequest) req, (HttpServletResponse) res));
        chain.doFilter(req, res);
    }
}
