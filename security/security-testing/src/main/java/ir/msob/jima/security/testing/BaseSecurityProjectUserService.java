package ir.msob.jima.security.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.msob.jima.platform.api.security.BaseClaimKey;
import ir.msob.jima.platform.api.security.BaseUser;
import ir.msob.jima.platform.api.security.BaseUserService;
import ir.msob.jima.platform.api.security.UserInfoCodec;
import ir.msob.jima.platform.api.util.Strings;
import ir.msob.jima.security.api.properties.SecurityProperties;
import lombok.SneakyThrows;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;


public interface BaseSecurityProjectUserService extends BaseUserService {
    SecurityProperties getSecurityProperties();

    ObjectMapper getObjectMapper();


    @Override
    default <USER extends BaseUser, A extends Authentication> @NonNull USER getUser(@NonNull A authentication) {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        return getUser(jwtAuthenticationToken.getTokenAttributes());
    }


    @SneakyThrows
    @Override
    default <USER extends BaseUser> @NonNull USER getUser(String userInfo, Class<USER> userClass) {
        return UserInfoCodec.decode(getObjectMapper(), userInfo, userClass);
    }

    @Override
    default <USER extends BaseUser, P extends Principal> @NonNull USER getUser(@NonNull P principal) {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
        Jwt jwt = jwtAuthenticationToken.getToken();
        return getUser(jwt.getClaims());
    }


    @Override
    default <USER extends BaseUser> @NonNull USER getUser(String userInfo, Map<String, Object> claims, Class<USER> userClass) {
        if (claims.get(BaseClaimKey.SUBJECT).equals(getSecurityProperties().getDefaultClientRegistrationId())) {
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
    default <USER extends BaseUser> @NonNull USER getUser(USER user, Map<String, Object> claims) {
        if (claims.get(BaseClaimKey.SUBJECT).equals(getSecurityProperties().getDefaultClientRegistrationId())) {
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
    default <USER extends BaseUser, P extends Principal> @NonNull USER getUser(String userInfo, @NonNull P principal, Class<USER> userClass) {
        if (Strings.isNotBlank(principal.getName())
                && Strings.isNotBlank(getSecurityProperties().getDefaultClientRegistrationId())
                && Objects.equals(principal.getName(), getSecurityProperties().getDefaultClientRegistrationId())) {
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
