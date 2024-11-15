package ir.msob.jima.security.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.msob.jima.core.beans.properties.JimaProperties;
import ir.msob.jima.core.commons.security.BaseUser;
import ir.msob.jima.core.commons.security.BaseUserService;
import ir.msob.jima.core.commons.security.ClaimKey;
import ir.msob.jima.core.commons.security.UserInfoUtil;
import lombok.SneakyThrows;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.security.Principal;
import java.util.Map;


public interface BaseSecurityProjectUserService extends BaseUserService {
    JimaProperties getJimaProperties();

    ObjectMapper getObjectMapper();


    @Override
    default <USER extends BaseUser, A extends Authentication> USER getUser(A authentication) {
        if (authentication != null) {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
            return getUser(jwtAuthenticationToken.getTokenAttributes());
        }
        throw new IllegalArgumentException("Authentication cannot be null. Please provide a valid authentication object.");
    }


    @SneakyThrows
    @Override
    default <USER extends BaseUser> USER getUser(String userInfo, Class<USER> userClass) {
        return UserInfoUtil.decodeUser(getObjectMapper(), userInfo, userClass);
    }

    @Override
    default <USER extends BaseUser, P extends Principal> USER getUser(P principal) {
        if (principal != null) {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
            Jwt jwt = jwtAuthenticationToken.getToken();

            return getUser(jwt.getClaims());
        }
        throw new IllegalArgumentException("Authentication cannot be null. Please provide a valid authentication object.");
    }


    @Override
    default <USER extends BaseUser> USER getUser(String userInfo, Map<String, Object> claims, Class<USER> userClass) {
        if (claims.get(ClaimKey.SUBJECT).equals(getJimaProperties().getSecurity().getDefaultClientRegistrationId())) {
            if (Strings.isNotBlank(userInfo)) {
                return getUser(userInfo, userClass);
            } else {
                throw new IllegalArgumentException("Authentication cannot be null. Please provide a valid authentication object.");
            }
        } else {
            return getUser(claims);
        }
    }

    @Override
    default <USER extends BaseUser> USER getUser(USER user, Map<String, Object> claims) {
        if (claims.get(ClaimKey.SUBJECT).equals(getJimaProperties().getSecurity().getDefaultClientRegistrationId())) {
            if (user != null) {
                return user;
            } else {
                throw new IllegalArgumentException("Authentication cannot be null. Please provide a valid authentication object.");
            }
        } else {
            return getUser(claims);
        }
    }

    @Override
    default <USER extends BaseUser, P extends Principal> USER getUser(String userInfo, P principal, Class<USER> userClass) {
        if (principal.getName().equals(getJimaProperties().getSecurity().getDefaultClientRegistrationId())) {
            if (Strings.isNotBlank(userInfo)) {
                return getUser(userInfo, userClass);
            } else {
                throw new IllegalArgumentException("Authentication cannot be null. Please provide a valid authentication object.");
            }
        } else {
            return getUser(principal);
        }
    }
}
