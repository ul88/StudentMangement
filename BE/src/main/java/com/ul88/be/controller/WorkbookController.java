package com.ul88.be.controller;

import com.ul88.be.dto.RequestUpdateWorkbookDto;
import com.ul88.be.service.ProblemService;
import com.ul88.be.service.WorkbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workbook")
@Log4j2
public class WorkbookController {
    private final WorkbookService workbookService;
    private final ProblemService problemService;


    @GetMapping
    public ResponseEntity<?> getWorkbooks() {
        return ResponseEntity.ok(workbookService.getWorkbooks());
    }

    @PostMapping
    public ResponseEntity<?> createWorkbook(@RequestBody String name) {
        workbookService.addWorkbook(name);
        return ResponseEntity.ok(name);
    }

    @PutMapping
    public ResponseEntity<?> updateWorkbook(@RequestBody RequestUpdateWorkbookDto requestUpdateWorkbookDto) {
        workbookService.updateWorkbook(requestUpdateWorkbookDto);
        return ResponseEntity.ok("success");
    }
}
