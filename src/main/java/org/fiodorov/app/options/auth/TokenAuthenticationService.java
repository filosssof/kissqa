package org.fiodorov.app.options.auth;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

/**
 * @author rfiodorov
 *         on 2/2/17.
 */
public class TokenAuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticationService.class);

    private static final String AUTH_HEADER_NAME = "X-Auth-Token";

    private static final String AUTH_HEADER_NAME_R = "X-Auth-Token-U";

    private final String secrete;

    private final Long expirationTime;

    public TokenAuthenticationService(String secret, String expirationTime) {
        secrete = secret;
        this.expirationTime = Long.valueOf(expirationTime);
    }

    public void addAuthentication(HttpServletResponse response, UserAuthentication authentication) {

        final UserToken user = authentication.getDetails();
        if (user != null) {
            Date issuedAt = new Date();
            response.setCharacterEncoding("UTF-8");
            response.addHeader(AUTH_HEADER_NAME, SecurityUtil.generateToken(user, secrete, expirationTime, issuedAt));
            response.addHeader(AUTH_HEADER_NAME_R, SecurityUtil.generateRefreshToken(user, secrete, issuedAt));
        }
    }

    public UserAuthentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {

        final String token = request.getHeader(AUTH_HEADER_NAME);
        if (token != null && !token.trim().isEmpty()) {
            final UserToken user = authenticateToken(token, response);
            if (user != null) {
                return new UserAuthentication(user);
            }
        }
        return null;
    }

    private UserToken authenticateToken(String expireToken, HttpServletResponse response) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(secrete))
                    .parseClaimsJws(expireToken).getBody();
            if (claims != null) {
                return SecurityUtil.generateTokenFromClaims(claims);
            }
        } catch (ExpiredJwtException ex) {
            response.setHeader("expired", "true");
            LOGGER.info(ex.getMessage(), ex);
        } catch (JwtException e) {
            LOGGER.error(e.getMessage(),e);
            return null;
        }
        return null;
    }
}
