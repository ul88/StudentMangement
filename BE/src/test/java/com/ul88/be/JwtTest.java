package com.ul88.be;

import com.ul88.be.Jwt.JwtUtil;
import com.ul88.be.dto.MemberDetails;
import com.ul88.be.entity.Member;
import com.ul88.be.entity.MemberRole;
import com.ul88.be.repository.MemberRepository;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class JwtTest {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Test
    public void jwtTest(){
        String token = jwtUtil.generateAccessToken(MemberDetails.builder()
                        .id("asd")
                        .name("asd")
                        .role(MemberRole.ADMIN)
                        .password("1234")
                        .email("asd@gmail.com")
                .build());

        jwtUtil.getAuthentication(token);
    }

    @Test
    public void addAdmin(){
        memberRepository.save(Member.builder()
                        .id("admin")
                        .password(passwordEncoder.encode("1234"))
                        .email("admin@ul88.org")
                        .name("admin")
                        .role(MemberRole.ADMIN)
                .build());
    }
}
