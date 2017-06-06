package org.fiodorov.app.options.auth;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fiodorov.app.options.ErrorCode;
import org.fiodorov.exception.ExceptionResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import io.jsonwebtoken.ExpiredJwtException;

/**
 * @author rfiodorov
 *         on 2/7/17.
 */
public class AuthenticationErrorHandler implements AuthenticationEntryPoint {
    public static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationErrorHandler.class);

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
            AuthenticationException e)
            throws ExpiredJwtException, IOException {
        ExceptionResponse res;
        if (httpServletResponse.getHeader("expired") != null) {
            res = new ExceptionResponse(ErrorCode.EXPIRED_TOKEN.getActualCode(),
                    ErrorCode.REQUEST_UNAUTHORIZED.getActualCode());
        } else {
            res = new ExceptionResponse(ErrorCode.REQUEST_UNAUTHORIZED.getActualCode());
        }
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.getWriter().print(new JSONObject(res));
    }

    public static void badCredential(HttpServletResponse response) {
        ExceptionResponse res = new ExceptionResponse(ErrorCode.BAD_CREDENTIAL.getActualCode());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        try {
            response.getWriter().print(new JSONObject(res));
        } catch (IOException io) {
            LOGGER.error(io.getLocalizedMessage(), io);
        }
    }

    public static void invalidTokenFormat(HttpServletResponse response) {
        ExceptionResponse res = new ExceptionResponse(ErrorCode.INVALID_TOKEN.getActualCode());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        try {
            response.getWriter().print(new JSONObject(res));
        } catch (IOException io) {
            LOGGER.error(io.getLocalizedMessage(), io);
        }
    }

    public static void differentToken(HttpServletResponse response) {
        ExceptionResponse res = new ExceptionResponse(ErrorCode.DIFFERENT_TOKEN.getActualCode());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        try {
            response.getWriter().print(new JSONObject(res));
        } catch (IOException io) {
            LOGGER.error(io.getLocalizedMessage(), io);
        }
    }

}
