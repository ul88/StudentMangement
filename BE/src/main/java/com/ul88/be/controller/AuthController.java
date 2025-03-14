package com.ul88.be.controller;

import com.ul88.be.dto.MemberDto;
import com.ul88.be.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;
    @PostMapping
    public ResponseEntity<?> login(@RequestBody MemberDto memberDto) {
        return ResponseEntity.ok(memberService.login(memberDto));
    }
}
