package sk.flowy.msbiexport.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpMethod.GET;

@Repository
public class TokenRepositoryImpl implements TokenRepository {

    private static final String AUTHORIZATION = "Authorization";

    @Value("${checkTokenUrl}")
    private String checkTokenUrl;

    private RestTemplate restTemplate;

    @Autowired
    public TokenRepositoryImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    @Cacheable(value = "token", unless = "#root.args[1]==true")
    @CacheEvict(value = "token", condition = "#root.args[1]==true", beforeInvocation = true)
    public CallResponse checkToken(String requestToken, boolean tokenExpired) {
        if (tokenExpired) {
            CallResponse callResponse = new CallResponse();
            callResponse.setError("Token is expired!");
            return callResponse;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, requestToken);

        ResponseEntity<CallResponse> responseEntity = restTemplate.exchange(checkTokenUrl, GET, new HttpEntity<>
                (headers), CallResponse.class);

        return responseEntity.getBody();
    }
}
