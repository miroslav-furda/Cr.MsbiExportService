package sk.flowy.msbiexport.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.GenericFilterBean;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Order(value = HIGHEST_PRECEDENCE)
@Component
public class SecurityFilter extends GenericFilterBean {

    private static final String AUTHORIZATION = "Authorization";
    private static final String HASH_NAME = "tokens";

    @Value("${checkTokenUrl}")
    private String checkTokenUrl;

    private final RedisTemplate<String, Date> redisTemplate;
    private HashOperations<String, String, Date> hashOperations;

    @Autowired
    public SecurityFilter(RedisTemplate<String, Date> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String bearerToken = request.getHeader(AUTHORIZATION);

        if (bearerToken == null) {
            response.sendError(UNAUTHORIZED.value(), "Missing token in Authorization header!");
            return;
        }

        Date expirationDate = hashOperations.get(HASH_NAME, bearerToken);
        if (expirationDate != null) {
            if (new Date().after(expirationDate)) {
                hashOperations.delete(HASH_NAME, bearerToken);
                response.sendError(UNAUTHORIZED.value(), "Token is expired!");
                return;
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, bearerToken);
        ResponseEntity<Response> responseEntity = restTemplate.exchange(checkTokenUrl, GET, new HttpEntity<>
                (headers), Response.class);

        if (responseEntity.getBody().getError() != null) {
            response.sendError(UNAUTHORIZED.value(), responseEntity.getBody().getError());
            return;
        }

        if (responseEntity.getBody().getSuccess() != null) {
            Date expiresAt = JWT.decode(bearerToken).getExpiresAt();
            hashOperations.put(HASH_NAME, bearerToken, expiresAt);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
