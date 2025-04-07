package com.ul88.be.security;

import com.ul88.be.dto.MemberDetails;
import com.ul88.be.service.MemberDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
        String accessToken = request.getHeader(JwtUtil.ACCESS_TOKEN_HEADER);
        String refreshToken = request.getHeader(JwtUtil.REFRESH_TOKEN_HEADER);
        if(refreshToken != null && refreshToken.startsWith(JwtUtil.AUTHORIZATION_PREFIX)) {
            refreshToken = refreshToken.substring(7).trim();
            log.info("refresh token을 이용한 검증 시작 {}", refreshToken);
            if(jwtUtil.validateToken(refreshToken)) {
                String userId = jwtUtil.getRefreshTokenSubject(refreshToken);
                MemberDetails member = (MemberDetails) memberDetailsService.loadUserByUsername(userId);

                String newAccessToken = JwtUtil.AUTHORIZATION_PREFIX +
                        jwtUtil.generateAccessToken(member);
                String newRefreshToken = JwtUtil.AUTHORIZATION_PREFIX +
                        jwtUtil.generateRefreshToken(member);

                Authentication auth = new UsernamePasswordAuthenticationToken(member, "", member.getAuthorities());

                request.setAttribute(JwtUtil.ACCESS_TOKEN_HEADER, newAccessToken);
                request.setAttribute(JwtUtil.REFRESH_TOKEN_HEADER, newRefreshToken);

                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("refresh token으로 access token 발급 완료");
            }else{
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }else if(accessToken != null && accessToken.startsWith(JwtUtil.AUTHORIZATION_PREFIX)) {
            accessToken = accessToken.substring(7).trim();
            log.info("access token을 이용한 검증 시작 {}", accessToken);
            if(jwtUtil.validateToken(accessToken)) {
                Authentication auth = jwtUtil.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(auth);
                String newAccessToken = JwtUtil.AUTHORIZATION_PREFIX +
                        jwtUtil.generateAccessToken((MemberDetails) auth.getPrincipal());
                request.setAttribute(JwtUtil.ACCESS_TOKEN_HEADER, newAccessToken);
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("access token을 이용해서 인증 패스");
            }else{
                if(request.getRequestURI().equals("/api/login")){
                    log.info("로그인 요청을 보냈으므로 로그인 필터로 넘김");
                    filterChain.doFilter(request, response);
                    return;
                }
                log.info("인증 실패 401 반환");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        log.info("jwtFilter 끝");
        filterChain.doFilter(request, response);
    }
}
