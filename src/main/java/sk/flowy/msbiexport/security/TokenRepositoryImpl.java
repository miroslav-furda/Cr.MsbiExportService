package sk.flowy.msbiexport.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

import static com.auth0.jwt.JWT.decode;
import static org.joda.time.LocalDate.now;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Default implementation of {@link TokenRepository} that uses spring cache.
 */
@Repository
public class TokenRepositoryImpl implements TokenRepository {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Value("${checkTokenUrl}")
    private String checkTokenUrl;

    private RestTemplate restTemplate;

    /**
     * Constructor.
     *
     * @param restTemplate autowired restTemplate
     */
    @Autowired
    public TokenRepositoryImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public CallResponse checkTokenValidity(String token) {
        Date expiresAt = decode(token).getExpiresAt();
        return checkToken(token, expiresAt.before(now().toDate()));
    }

    @Cacheable(value = "token", unless = "#root.args[1]==true")
    @CacheEvict(value = "token", condition = "#root.args[1]==true", beforeInvocation = true)
    public CallResponse checkToken(String token, boolean tokenExpired) {
        if (tokenExpired) {
            CallResponse callResponse = new CallResponse();
            callResponse.setError("Token is expired!");
            return callResponse;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, BEARER_PREFIX + token);

        ResponseEntity<CallResponse> responseEntity;
        try {
            responseEntity = restTemplate.exchange(checkTokenUrl, GET, new HttpEntity<>(headers), CallResponse.class);
        } catch (HttpClientErrorException e) {
            responseEntity = new ResponseEntity<>(new CallResponse(null, e.getMessage()), BAD_REQUEST);
        }

        return responseEntity.getBody();
    }
}
