package com.ul88.be.controller;

import com.ul88.be.dto.ProblemDto;
import com.ul88.be.service.ProblemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/problem")
public class ProblemController {
    private final ProblemService problemService;

    @GetMapping("/{id}") // student id
    public List<ProblemDto> getProblemSameStudentId(@PathVariable Long id){
        return problemService.getStudentInProblems(id);
    }
}
