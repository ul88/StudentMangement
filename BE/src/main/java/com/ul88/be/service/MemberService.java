package com.ul88.be.service;

import com.ul88.be.Jwt.JwtUtil;
import com.ul88.be.dto.MemberDetails;
import com.ul88.be.dto.MemberDto;
import com.ul88.be.entity.Member;
import com.ul88.be.exception.CustomException;
import com.ul88.be.exception.ErrorCode;
import com.ul88.be.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String login(MemberDto memberDto){
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberDto.getId(),
                memberDto.getPassword());
       MemberDetails memberDetails = findById(memberDto.getId());

       if(passwordEncoder.encode(memberDto.getPassword()).equals(memberDetails.getPassword())){
           Authentication auth = new UsernamePasswordAuthenticationToken(authentication, authentication.getDetails(), authentication.getAuthorities());
           MemberDetails m = (MemberDetails) authentication.getPrincipal();
           String token = jwtUtil.generateAccessToken(m);
           return "success login";
       }
       return "false";
    }

    public MemberDetails findById(String id){
        Member m = memberRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.PK_NOT_FOUND));

        return MemberDetails.builder()
                .id(m.getId())
                .name(m.getName())
                .password(m.getPassword())
                .role(m.getRole())
                .build();
    }
}
