package org.fiodorov.app.options.auth;

import java.io.IOException;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.RandomStringUtils;
import org.fiodorov.app.options.ErrorCode;
import org.fiodorov.exception.ExceptionResponse;
import org.fiodorov.exception.UserDeletedException;
import org.fiodorov.exception.UserInBlackListException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author rfiodorov
 *         on 2/2/17.
 */
public class SecurityUtil {

    /**
     * Sets the length of temporary password that is going to be generated on user creation.
     */
    private static final int GENERATED_PASSWORD_LENGTH = 8;

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityUtil.class);

    private SecurityUtil() {
    }

    public static void authenticateUser(TokenAuthenticationService tokenAuthenticationService,
            HttpServletResponse response, UserToken authenticatedUser) throws IOException {

        UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);
        tokenAuthenticationService.addAuthentication(response, userAuthentication);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);

    }

    public static void regenerateAuthenticationToken(
            TokenAuthenticationService tokenAuthenticationService, HttpServletResponse response,
            UserToken authenticatedUser) throws IOException {
        UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);
        tokenAuthenticationService.addAuthentication(response, userAuthentication);
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
    }

    public static String generateToken(UserToken authenticatedUser, String secrete, Long expirationTime, Date iat) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secrete);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(iat)
                .signWith(signatureAlgorithm, signingKey);
        generateClaims(authenticatedUser, builder);

        long expMillis = nowMillis + expirationTime;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp);

        LOGGER.debug("generate new refresh token for user: {}, {}", authenticatedUser.getUsername(), builder.compact());
        return builder.compact();
    }

    /**
     * Generates temporary password used on user creation.
     */
    public static String generateTemporaryPassword() {
        return RandomStringUtils.random(GENERATED_PASSWORD_LENGTH, 0, 0, true, true, null, new SecureRandom());
    }

    public static String generateRefreshToken(UserToken authenticatedUser, String secrete, Date iat) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secrete);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(iat)
                .signWith(signatureAlgorithm, signingKey);
        generateClaims(authenticatedUser, builder);
        LOGGER.debug("generate new refresh token for user: {}, {}", authenticatedUser.getUsername(), builder.compact());
        return builder.compact();
    }

    private static void generateClaims(UserToken authenticatedUser, JwtBuilder builder) {
        builder.claim("username", authenticatedUser.getUsername());
        builder.claim("roles", authenticatedUser.getRoles());
        builder.claim("id", authenticatedUser.getId());
    }

    public static UserToken generateTokenFromClaims(Claims claims) {
        return new UserToken(claims);
    }

    public static Authentication authenticate(AuthenticationManager manager, UsernamePasswordAuthenticationToken token,
            HttpServletResponse response) {
        try {
            return manager.authenticate(token);
        } catch (BadCredentialsException e) {
            LOGGER.error(e.getMessage(), e);
            return getBadCredentialException(response);
        } catch (DisabledException e) {
            LOGGER.error(e.getMessage(), e);
            getDisabledUserException(response);
        } catch (LockedException e) {
            LOGGER.error(e.getMessage(), e);
            return getLockedUserException(response);
        } catch (UserDeletedException e) {
            LOGGER.error(e.getMessage(), e);
            return getDeletedUserException(response);
        } catch (UserInBlackListException e) {
            LOGGER.error(e.getMessage(), e);
            return getBlackListUserException(response);
        } catch (InternalAuthenticationServiceException e) {
            LOGGER.error(e.getMessage(), e);
            return getUserIsFacebookAccountExecption(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            Throwable cause = e.getCause();
            if (cause instanceof UserDeletedException) {
                getDeletedUserException(response);
            } else if (cause instanceof UserInBlackListException) {
                getBlackListUserException(response);
            } else {
                getGenericException(response);
            }
        }
        return null;
    }

    private static Authentication getUserIsFacebookAccountExecption(HttpServletResponse response) {
        ExceptionResponse res = new ExceptionResponse(ErrorCode.FACEBOOK_ACCOUNT_EXCEPTION.getActualCode());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        try {
            response.getWriter().print(new JSONObject(res));
        } catch (IOException io) {
            LOGGER.error(io.getLocalizedMessage(), io);
        }
        return null;
    }

    private static Authentication getGenericException(HttpServletResponse response) {
        ExceptionResponse res = new ExceptionResponse(ErrorCode.GENERIC_AUTHENTICATION_EXCEPTION.getActualCode());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        try {
            response.getWriter().print(new JSONObject(res));
        } catch (IOException io) {
            LOGGER.error(io.getLocalizedMessage(), io);
        }
        return null;
    }

    private static Authentication getBlackListUserException(HttpServletResponse response) {
        ExceptionResponse res = new ExceptionResponse(ErrorCode.BLACK_LIST_USER.getActualCode());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        try {
            response.getWriter().print(new JSONObject(res));
        } catch (IOException io) {
            LOGGER.error(io.getLocalizedMessage(), io);
        }
        return null;
    }

    private static Authentication getDeletedUserException(HttpServletResponse response) {
        ExceptionResponse res = new ExceptionResponse(ErrorCode.DELETED_USER.getActualCode());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        try {
            response.getWriter().print(new JSONObject(res));
        } catch (IOException io) {
            LOGGER.error(io.getLocalizedMessage(), io);
        }
        return null;
    }

    private static Authentication getLockedUserException(HttpServletResponse response) {
        ExceptionResponse res = new ExceptionResponse(ErrorCode.LOCKED_USER.getActualCode());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        try {
            response.getWriter().print(new JSONObject(res));
        } catch (IOException io) {
            LOGGER.error(io.getLocalizedMessage(), io);
        }
        return null;
    }

    private static Authentication getDisabledUserException(HttpServletResponse response) {
        ExceptionResponse res = new ExceptionResponse(ErrorCode.DISABLED_USER.getActualCode());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        try {
            response.getWriter().print(new JSONObject(res));
        } catch (IOException io) {
            LOGGER.error(io.getLocalizedMessage(), io);
        }
        return null;
    }

    private static Authentication getBadCredentialException(HttpServletResponse response) {
        ExceptionResponse res = new ExceptionResponse(ErrorCode.BAD_CREDENTIAL.getActualCode());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        try {
            response.getWriter().print(new JSONObject(res));
        } catch (IOException io) {
            LOGGER.error(io.getLocalizedMessage(), io);
        }
        return null;
    }
}
