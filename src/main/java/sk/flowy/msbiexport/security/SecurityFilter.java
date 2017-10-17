package sk.flowy.msbiexport.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.auth0.jwt.JWT.decode;
import static org.joda.time.LocalDate.now;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
@Order(value = HIGHEST_PRECEDENCE)
public class SecurityFilter extends GenericFilterBean {

    private static final String AUTHORIZATION = "Authorization";
    private static final Integer BEARER_OFFSET = 7;
    private static final String TOKEN_REGEX = "^Bearer .+";

    private TokenRepository tokenRepository;

    @Autowired
    public SecurityFilter(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestToken = request.getHeader(AUTHORIZATION);
        if (requestToken == null) {
            response.sendError(BAD_REQUEST.value(), "Missing token!");
            return;
        }

        if (!requestToken.matches(TOKEN_REGEX)) {
            response.sendError(BAD_REQUEST.value(), "Invalid token!");
            return;
        }

        String token = requestToken.substring(BEARER_OFFSET);

        try {
            decode(token);
        } catch (JWTDecodeException e) {
            response.sendError(UNAUTHORIZED.value(), "Invalid token!");
            return;
        }
        Date expiresAt = decode(token).getExpiresAt();
        CallResponse callResponse = tokenRepository.checkToken(requestToken, expiresAt.before(now().toDate()));

        if (callResponse.hasError()) {
            response.sendError(UNAUTHORIZED.value(), callResponse.getError());
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
