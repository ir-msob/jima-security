package ir.msob.jima.security.api.restful.jwt;

import ir.msob.jima.core.beans.properties.JimaProperties;
import ir.msob.jima.core.commons.security.BaseTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TokenService implements BaseTokenService {

    private final JimaProperties jimaProperties;
    private final BaseJwtTokenService jwtTokenService;

    @Override
    public String getToken() {
        return jwtTokenService.generateToken(
                jimaProperties.getSecurity().getClientCredentialsToken().getSecret(),
                jimaProperties.getSecurity().getClientCredentialsToken().getExpiration(),
                jimaProperties.getSecurity().getClientCredentialsToken().getAlgorithm(),
                jimaProperties.getSecurity().getClientCredentialsToken().getClaims());
    }
}
