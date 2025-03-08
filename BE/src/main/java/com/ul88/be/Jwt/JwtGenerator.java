//package com.ul88.be.Jwt;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.impl.DefaultClaims;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import org.apache.tomcat.util.codec.binary.Base64;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class JwtGenerator {
//    private final Key key;
//    private final Long accessExpiration;
//    private final Long refreshExpiration;
//
//    public JwtGenerator(@Value("jwt.secret")String secretKey,
//                        @Value("jwt.expiration_time") Long expiration) {
//        this.accessExpiration = expiration;
//
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        this.key = Keys.hmacShaKeyFor(keyBytes);
//        this.refreshExpiration = expiration*1000;
//    }
//
//    public String generateAccessToken(User user) {
//        Long now = System.currentTimeMillis();
//        return Jwts.builder()
//                .setHeaderParam("typ", "JWT")
//                .setHeaderParam("alg", "HS256")
//                .setSubject(user.getUsername())
//                .setClaims(createUserClaims(user))
//                .setIssuedAt(new Date(now))
//                .setExpiration(new Date(now + accessExpiration))
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public String generateRefreshToken(User user){
//        Long now = System.currentTimeMillis();
//        return Jwts.builder()
//                .setHeaderParam("typ", "JWT")
//                .setHeaderParam("alg", "HS256")
//                .setSubject(user.getUsername())
//                .setIssuedAt(new Date(now))
//                .setExpiration(new Date(now + refreshExpiration))
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    private Map<String, Object> createUserClaims(User user) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("roles", user.getAuthorities().iterator().next().getAuthority());
//        return claims;
//    }
//}
