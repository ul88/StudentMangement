package com.ul88.be.dto;

import com.ul88.be.entity.Member;
import com.ul88.be.entity.MemberRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private String id;
    private String name;
    private String email;
    private String password;

    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .id(id)
                .password(passwordEncoder.encode(password))
                .name(name)
                .email(email)
                .build();
    }
}
