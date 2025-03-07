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

    @GetMapping // student id
    public ResponseEntity<List<ProblemDto>> getProblemSameStudentId(@RequestParam(value = "studentId", required = false) Long studentId,
                                                                    @RequestParam(value = "workbookId", required = false) Long workbookId){
        log.info(studentId + " " + workbookId);
        if(studentId != null && workbookId != null){
            return ResponseEntity.ok(problemService.getStudentSolvedProblemsInWorkbook(studentId, workbookId));
        }else if(studentId != null){
            return ResponseEntity.ok(problemService.getProblemsInStudent(studentId));
        }else if(workbookId != null){
            return ResponseEntity.ok(problemService.getProblemsInWorkbook(workbookId));
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
