package sk.codexa.cr.msbiexport.security;

import com.auth0.jwt.JWT;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import static com.auth0.jwt.algorithms.Algorithm.HMAC256;
import static java.util.Calendar.DATE;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SecurityFilterTest.TestConfiguration.class})
public class SecurityFilterTest {
    private static final String SERVER_NAME = "server";
    private static final String VALID_TOKEN = "Bearer " +
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjYyLCJpc3MiOiJodHRwOlwvXC9sYWJhcy5wcm9taXNlby5jb21cL2FwcFwvcHVibGljXC9hcGlcL2F1dGhlbnRpY2F0ZSIsImlhdCI6MTUwODIzMjIyMSwiZXhwIjoxNTA4NDI0MjIxLCJuYmYiOjE1MDgyMzIyMjEsImp0aSI6IjdlMmQ1NTg2Yzg4YjAzMzE0NDAzZDRmM2U3ODIyMjAzIn0.ujt2PXYrlmHVcL1GZo5ByZHcHdBaPr-dkCgtvBx8uG8";
    private static final String INVALID_FORMAT_TOKEN = "Bearer invalid";
    private static final String INVALID_TOKEN = "Bearer " +
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjYyLCJpc3MiOiJodHRwOlwvXC9sYWJhcy5wcm9taXNlby5jb21cL2FwcFwvcHVibGljXC9hcGlcL2F1dGhlbnRpY2F0ZSIsImlhdCI6MTUwODI1NDg2NiwiZXhwIjoxNTA4NDQ2ODY2LCJuYmYiOjE1MDgyNTQ4NjYsImp0aSI6ImMxNjUyYjFiZGVkNDY5MWI1MzMzYjlmZDY5NjlhMWE0In0.vRvJO-75yJtXAHYFA6hybz9puwQ0piieECHWqofu5hM";
    private static final String NOT_BEARER_TOKEN = "invalid";
    private static final String AUTHORIZATION = "Authorization";

    @Autowired
    private RestTemplate restTemplate;

    private SecurityFilter securityFilter;
    private ServletRequest servletRequest;
    private ServletResponse servletResponse;
    private FilterChain filterChain;
    private String expiredToken;

    @Before
    public void setup() throws UnsupportedEncodingException {
        servletRequest = mock(HttpServletRequest.class);
        servletResponse = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);

        Calendar cal = Calendar.getInstance();
        cal.add(DATE, -1);
        cal.getTime();
        expiredToken = "Bearer " + JWT.create().withExpiresAt(cal.getTime()).sign(HMAC256("secret"));

        TokenRepositoryImpl tokenRepository = new TokenRepositoryImpl(restTemplate);
        setField(tokenRepository, "checkTokenUrl", SERVER_NAME);
        securityFilter = new SecurityFilter(tokenRepository);

        HttpHeaders rightHeader = new HttpHeaders();
        rightHeader.add(AUTHORIZATION, VALID_TOKEN);
        when(restTemplate.exchange(SERVER_NAME, GET, new HttpEntity<>(rightHeader), CallResponse.class))
                .thenReturn(new ResponseEntity<>(new CallResponse("true", null), OK));

        HttpHeaders wrongHeader = new HttpHeaders();
        wrongHeader.add(AUTHORIZATION, INVALID_TOKEN);
        when(restTemplate.exchange(SERVER_NAME, GET, new HttpEntity<>(wrongHeader), CallResponse.class))
                .thenReturn(new ResponseEntity<>(new CallResponse(null, "token_invalid"), BAD_REQUEST));
    }

    @Test
    public void when_authorization_header_is_not_present_then_bad_request_is_send_in_response() throws IOException,
            ServletException {
        when(((HttpServletRequest) servletRequest).getHeader(AUTHORIZATION)).thenReturn(null);
        securityFilter.doFilter(servletRequest, servletResponse, filterChain);

        verify(((HttpServletResponse) servletResponse)).sendError(BAD_REQUEST.value(), "Missing token!");
    }

    @Test
    public void when_authorization_header_is_not_bearer_then_bad_request_is_send_in_response() throws IOException,
            ServletException {
        when(((HttpServletRequest) servletRequest).getHeader(AUTHORIZATION)).thenReturn(NOT_BEARER_TOKEN);
        securityFilter.doFilter(servletRequest, servletResponse, filterChain);

        verify(((HttpServletResponse) servletResponse)).sendError(BAD_REQUEST.value(), "Invalid token!");
    }

    @Test
    public void when_authorization_token_is_invalid_exception_is_thrown() throws IOException, ServletException {
        when(((HttpServletRequest) servletRequest).getHeader(AUTHORIZATION)).thenReturn(INVALID_FORMAT_TOKEN);
        securityFilter.doFilter(servletRequest, servletResponse, filterChain);

        verify(((HttpServletResponse) servletResponse)).sendError(UNAUTHORIZED.value(), "Invalid token!");
    }

    @Test
    public void when_token_is_expired_than_unauthorized_is_returned() throws IOException, ServletException {
        when(((HttpServletRequest) servletRequest).getHeader(AUTHORIZATION)).thenReturn(expiredToken);
        securityFilter.doFilter(servletRequest, servletResponse, filterChain);

        verify(((HttpServletResponse) servletResponse)).sendError(UNAUTHORIZED.value(), "Token is expired!");
    }

    @Test
    public void when_response_has_error_then_unauthorized_is_returned() throws IOException, ServletException {
        when(((HttpServletRequest) servletRequest).getHeader(AUTHORIZATION)).thenReturn(INVALID_TOKEN);
        securityFilter.doFilter(servletRequest, servletResponse, filterChain);

        verify(((HttpServletResponse) servletResponse)).sendError(UNAUTHORIZED.value(), "token_invalid");
    }

    @Test
    public void checking_token_is_invoked_when_token_is_valid() throws IOException, ServletException {
        when(((HttpServletRequest) servletRequest).getHeader(AUTHORIZATION)).thenReturn(VALID_TOKEN);
        securityFilter.doFilter(servletRequest, servletResponse, filterChain);

        verify(restTemplate, times(1)).exchange(anyString(), anyObject(), anyObject(),
                eq(CallResponse.class));
        verify(filterChain, times(1)).doFilter(servletRequest, servletResponse);
    }

    @Configuration
    static class TestConfiguration {

        @Bean
        public RestTemplate restTemplate() {
            return mock(RestTemplate.class);
        }
    }
}