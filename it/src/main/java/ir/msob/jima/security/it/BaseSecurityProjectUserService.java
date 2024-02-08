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

import java.io.Serializable;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;


public interface BaseSecurityProjectUserService extends BaseUserService {
    JimaProperties getJimaProperties();

    ObjectMapper getObjectMapper();


    @Override
    default <ID extends Comparable<ID> & Serializable, USER extends BaseUser<ID>, A extends Authentication> Optional<USER> getUser(A authentication) {
        if (authentication != null) {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
            return getUser(jwtAuthenticationToken.getTokenAttributes());
        }
        return Optional.empty();
    }


    @SneakyThrows
    @Override
    default <ID extends Comparable<ID> & Serializable, USER extends BaseUser<ID>> Optional<USER> getUser(String userInfo, Class<USER> userClass) {
        return UserInfoUtil.decodeUser(getObjectMapper(), userInfo, userClass);
    }

    @Override
    default <ID extends Comparable<ID> & Serializable, USER extends BaseUser<ID>, P extends Principal> Optional<USER> getUser(P principal) {
        if (principal != null) {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
            Jwt jwt = jwtAuthenticationToken.getToken();

            return getUser(jwt.getClaims());
        }
        return Optional.empty();
    }


    @Override
    default <ID extends Comparable<ID> & Serializable, USER extends BaseUser<ID>> Optional<USER> getUser(String userInfo, Map<String, Object> claims, Class<USER> userClass) {
        if (claims.get(ClaimKey.SUBJECT).equals(getJimaProperties().getSecurity().getDefaultClientRegistrationId())) {
            if (Strings.isNotBlank(userInfo)) {
                return getUser(userInfo, userClass);
            } else {
                return Optional.empty();
            }
        } else {
            return getUser(claims);
        }
    }

    @Override
    default <ID extends Comparable<ID> & Serializable, USER extends BaseUser<ID>> Optional<USER> getUser(USER user, Map<String, Object> claims) {
        if (claims.get(ClaimKey.SUBJECT).equals(getJimaProperties().getSecurity().getDefaultClientRegistrationId())) {
            if (user != null) {
                return Optional.of(user);
            } else {
                return Optional.empty();
            }
        } else {
            return getUser(claims);
        }
    }

    @Override
    default <ID extends Comparable<ID> & Serializable, USER extends BaseUser<ID>, P extends Principal> Optional<USER> getUser(String userInfo, P principal, Class<USER> userClass) {
        if (principal.getName().equals(getJimaProperties().getSecurity().getDefaultClientRegistrationId())) {
            if (Strings.isNotBlank(userInfo)) {
                return getUser(userInfo, userClass);
            } else {
                return Optional.empty();
            }
        } else {
            return getUser(principal);
        }
    }


}
