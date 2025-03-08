package com.ul88.be.controller;

import com.ul88.be.service.ManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/management")
@RequiredArgsConstructor
@Log4j2
public class ManagementController {
    private final ManagementService managementService;

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long id){
        return ResponseEntity.ok(managementService.getSolvedProblemsByStudent(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> renew(@PathVariable("id") Long id) throws IOException {
        managementService.renewStudent(id);
        return ResponseEntity.ok("success");
    }
}
