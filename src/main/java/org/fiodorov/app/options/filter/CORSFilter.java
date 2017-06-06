package org.fiodorov.app.options.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

/**
 * Created by rfiodorov on 6/18/16.
 */
@Component
public class CORSFilter implements Filter {

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // override enforced but not used
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) resp;
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin","*");//todo fix this after
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, HEAD, PATCH,PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, X-Auth-Token, X-Auth-Token-U");
        response.setHeader("Access-Control-Expose-Headers", "X-Auth-Token, X-Auth-Token-U");
        if (!"OPTIONS".equals(((HttpServletRequest) req).getMethod())) {
            chain.doFilter(req, resp);
        }
    }

    @Override
    public void destroy() {
        // override enforced but not used
    }

}
