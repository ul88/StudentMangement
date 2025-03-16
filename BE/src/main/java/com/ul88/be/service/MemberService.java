package com.ul88.be.service;

import com.ul88.be.dto.MemberDto;
import com.ul88.be.entity.Member;
import com.ul88.be.entity.MemberRole;
import com.ul88.be.exception.CustomException;
import com.ul88.be.exception.ErrorCode;
import com.ul88.be.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(MemberDto memberDto) {
        if(memberRepository.existsById(memberDto.getId())) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }else{
            memberRepository.save(memberDto.toEntity(passwordEncoder));
        }
    }

    public void changeRole(String id, String role) {
        Member member = memberRepository.findById(id).orElseThrow(()
                -> new CustomException(ErrorCode.USER_NOT_FOUND));

        member.changeRole(MemberRole.valueOf(role));
        memberRepository.save(member);
    }
}
