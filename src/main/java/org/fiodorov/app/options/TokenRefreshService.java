package org.fiodorov.app.options;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.fiodorov.app.options.auth.AuthenticationErrorHandler;
import org.fiodorov.app.options.auth.SecurityUtil;
import org.fiodorov.app.options.auth.UserAuthentication;
import org.fiodorov.app.options.auth.UserToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

/**
 * @author rfiodorov
 *         on 2/7/17.
 */
public class TokenRefreshService {
    public static final Logger LOGGER = LoggerFactory.getLogger(TokenRefreshService.class);
    private static final String AUTH_HEADER_NAME = "X-Auth-Token";
    private static final String AUTH_HEADER_NAME_R = "X-Auth-Token-U";
    private final String secrete;
    private final Long expirationTime;


    public TokenRefreshService(String secrete, String expirationTime) {
        this.secrete = secrete;
        this.expirationTime = Long.valueOf(expirationTime);
    }


    public UserAuthentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.debug("Attempt authentication for refresh token: ");
        final String token = request.getHeader(AUTH_HEADER_NAME_R);
        if (token != null && !token.trim().isEmpty()) {
            final UserToken user = authenticateToken(token);
            if (user != null) {
                LOGGER.debug("Attempt authentication for refresh token for username: {} ", user.getUsername());
                return new UserAuthentication(user);
            } else {
                AuthenticationErrorHandler.badCredential(response);
            }
        }
        return null;
    }

    public void addAuthentication(HttpServletRequest request, HttpServletResponse response, UserAuthentication authentication) {
        LOGGER.debug("Successful refresh token: ");
        final UserToken user = authentication.getDetails();
        response.setCharacterEncoding("UTF-8");
        if (isPair(request.getHeader(AUTH_HEADER_NAME), request.getHeader(AUTH_HEADER_NAME_R), response)) {
            Date issuedAt = new Date();
            response.addHeader(AUTH_HEADER_NAME, SecurityUtil.generateToken(user, secrete, expirationTime, issuedAt));
            response.addHeader(AUTH_HEADER_NAME_R, SecurityUtil.generateRefreshToken(user, secrete, issuedAt));
        } else {
            AuthenticationErrorHandler.differentToken(response);
        }
    }

    private Boolean isPair(String epiredToken, String refreshToken, HttpServletResponse response) {
        try {
            Claims old = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(secrete))
                    .parseClaimsJws(epiredToken).getBody();
            Claims refresh = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(secrete))
                    .parseClaimsJws(refreshToken).getBody();
            return old.getOrDefault("iat", null).toString().equals(refresh.getOrDefault("iat", null).toString());

        } catch (ExpiredJwtException e) {
            LOGGER.info(e.getMessage(),e);
            Claims refresh = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(secrete))
                    .parseClaimsJws(refreshToken).getBody();
            return e.getClaims().getOrDefault("iat", null).toString().equals(refresh.getOrDefault("iat", null).toString());
        } catch (JwtException ex) {
            LOGGER.error(ex.getMessage(),ex);
            AuthenticationErrorHandler.invalidTokenFormat(response);
        }
        return false;
    }

    private UserToken authenticateToken(String expireToken) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(secrete))
                    .parseClaimsJws(expireToken).getBody();
            if (claims != null) {
                return SecurityUtil.generateTokenFromClaims(claims);
            }
            return null;
        } catch (JwtException ex) {
            LOGGER.error("Invalid token type", ex);
            return null;
        }
    }

}
