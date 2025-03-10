package com.ul88.be.controller;

import com.ul88.be.dto.ProblemDto;
import com.ul88.be.service.ProblemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/problem")
public class ProblemController {
    private final ProblemService problemService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudents(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(problemService.getStudents(id));
    }
}
