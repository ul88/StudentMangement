package com.ul88.be.service;

import com.ul88.be.dto.MemberDetails;
import com.ul88.be.entity.Member;
import com.ul88.be.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findById(username).orElseThrow(() ->
                new UsernameNotFoundException(username));

        return MemberDetails.builder()
                .id(member.getId())
                .password(member.getPassword())
                .email(member.getEmail())
                .role(member.getRole())
                .build();
    }
}
