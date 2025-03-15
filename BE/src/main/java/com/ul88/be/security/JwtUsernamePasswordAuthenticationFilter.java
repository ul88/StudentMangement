package com.ul88.be.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ul88.be.Jwt.JwtUtil;
import com.ul88.be.dto.MemberDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Log4j2
public class JwtUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/api/login", "POST");
    private static final String CONTENT_TYPE = "application/json";
    private String usernameParameter = "id";
    private String passwordParameter = "password";
    private JwtUtil jwtUtil;
    @Setter
    private boolean postOnly = true;

    public JwtUsernamePasswordAuthenticationFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }

    public JwtUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,
                                                   JwtUtil jwtUtil) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if(request.getContentType() == null || !request.getContentType().startsWith(CONTENT_TYPE)) {
            throw new AuthenticationServiceException("Unsupported content type: " + request.getContentType());
        }
        else if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String requestBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            Map<String, String> usernamePassword = objectMapper.readValue(requestBody, Map.class);

            log.info("로그인 시작");
            String username = usernamePassword.get(getUsernameParameter());
            username = username != null ? username.trim() : "";
            String password = usernamePassword.get(getPasswordParameter());
            password = password != null ? password : "";
            log.info("아이디 : {}, 비밀번호 : {}", username, password);
            UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
            this.setDetails(request, authRequest);
            log.info("로그인 정보 : {}", authRequest.getPrincipal());
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("인증 완료");
        String accessToken = jwtUtil.generateAccessToken((MemberDetails) authResult.getPrincipal());
        String refreshToken = jwtUtil.generateRefreshToken((MemberDetails) authResult.getPrincipal());
        HttpSession session = request.getSession();
        session.setAttribute(JwtUtil.SESSION_NAME, refreshToken);
        log.info("토큰 {}", accessToken);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, JwtUtil.AUTHORIZATION_PREFIX + accessToken);
    }

    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public void setUsernameParameter(String usernameParameter) {
        Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
        this.usernameParameter = usernameParameter;
    }

    public void setPasswordParameter(String passwordParameter) {
        Assert.hasText(passwordParameter, "Password parameter must not be empty or null");
        this.passwordParameter = passwordParameter;
    }

    public final String getUsernameParameter() {
        return this.usernameParameter;
    }

    public final String getPasswordParameter() {
        return this.passwordParameter;
    }
}
