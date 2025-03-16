package com.ul88.be.controller;

import com.ul88.be.dto.MemberDto;
import com.ul88.be.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody MemberDto memberDto) {
        memberService.signup(memberDto);
        return ResponseEntity.ok("signup success");
    }

    @PostMapping("/admin")
    public ResponseEntity<?> admin(@RequestParam("id") String id,
                                   @RequestParam(value = "role", defaultValue = "USER") String role) {
        //id에 맞는 유저의 권한을 USER로 설정
        memberService.changeRole(id, role);
        return ResponseEntity.ok("change success");
    }
}
