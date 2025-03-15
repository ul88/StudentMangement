package com.ul88.be.Jwt;

import com.ul88.be.dto.MemberDetails;
import com.ul88.be.entity.MemberRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
@Transactional(readOnly = true)
public class JwtUtil {
    public static final String AUTHORIZATION_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String SESSION_NAME = "jwt";
    private final Key key;
    private final Long accessExpiration;
    private final Long refreshExpiration;

    public JwtUtil(@Value("${jwt.secret}")String secretKey,
                   @Value("${jwt.access_token_expiration_time}") Long accessExpiration,
                   @Value("${jwt.refresh_token_expiration_time}") Long refreshExpiration) {
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(MemberDetails memberDetails) {
        Long now = System.currentTimeMillis();
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setClaims(createUserClaims(memberDetails))
                .setSubject(memberDetails.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + accessExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(MemberDetails memberDetails){
        Long now = System.currentTimeMillis();
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setSubject(memberDetails.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + refreshExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getRefreshTokenSubject(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        ArrayList<GrantedAuthority> auth = new ArrayList<>();
        auth.add(new SimpleGrantedAuthority(claims.get("roles", String.class)));
        MemberDetails principal = MemberDetails.builder()
                .id(claims.getSubject())
                .role(MemberRole.valueOf(claims.get("roles", String.class).substring(5)))
                .build();
        return new UsernamePasswordAuthenticationToken(principal, token, auth);
    }

    private Map<String, Object> createUserClaims(MemberDetails memberDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", memberDetails.getAuthorities().iterator().next().getAuthority());
        return claims;
    }


    public boolean validateToken(String token) {
        if(token == null || token.isEmpty()){
            log.error("Jwt token is empty");
            return false;
        }
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT Token");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT Token format");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT Token");
        } catch (SecurityException e) {
            log.error("Invalid JWT signature");
        } catch (IllegalArgumentException e) {
            log.error("JWT Token is invalid or empty");
        }
        return false;
    }
}
