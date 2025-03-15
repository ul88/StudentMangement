package com.ul88.be.security;

import com.ul88.be.Jwt.JwtUtil;
import com.ul88.be.dto.MemberDetails;
import com.ul88.be.service.MemberDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final MemberDetailsService memberDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("jwFilter 시작");
        String token = request.getHeader(JwtUtil.AUTHORIZATION_HEADER);
        if (token != null && token.startsWith(JwtUtil.AUTHORIZATION_PREFIX)) {
            token = token.substring(7).trim();
        }
        if (jwtUtil.validateToken(token)) {
            Authentication authentication = jwtUtil.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("access 토큰이 있으므로 인증 패스");
        } else {
            // refresh 토큰으로 확인
            HttpSession session = request.getSession();
            String refreshToken = String.valueOf(session.getAttribute(JwtUtil.SESSION_NAME));
            log.info("refresh token : {}",refreshToken);
            if(refreshToken != null && jwtUtil.validateToken(refreshToken)) {
                String subject = jwtUtil.getRefreshTokenSubject(refreshToken);
                MemberDetails memberDetails = (MemberDetails) memberDetailsService.loadUserByUsername(subject);
                String accessToken = jwtUtil.generateAccessToken(memberDetails);
                String newRefreshToken = jwtUtil.generateRefreshToken(memberDetails);
                response.setHeader(JwtUtil.AUTHORIZATION_HEADER, JwtUtil.AUTHORIZATION_PREFIX + accessToken);
                log.info(session.getAttribute(JwtUtil.SESSION_NAME));
                session.setAttribute(JwtUtil.SESSION_NAME, newRefreshToken);
                log.info(session.getAttribute(JwtUtil.SESSION_NAME));
                log.info(session);
                log.info("refresh 토큰을 이용해서 access 토큰 발급");
            }
        }
        log.info("jwtFilter 끝");
        filterChain.doFilter(request, response);
    }
}
