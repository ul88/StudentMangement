package com.ul88.be.controller;

import com.ul88.be.dto.RequestSaveWorkbookDto;
import com.ul88.be.dto.RequestUpdateWorkbookDto;
import com.ul88.be.dto.ResponseWorkbookDto;
import com.ul88.be.dto.WorkbookDto;
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

    @GetMapping
    public ResponseEntity<?> getWorkbooks() {
        return ResponseEntity.ok(workbookService.getWorkbooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWorkbook(@PathVariable Long id) {

        WorkbookDto workbookDto = workbookService.getWorkbook(id);
        ResponseWorkbookDto response = ResponseWorkbookDto.builder()
                .id(workbookDto.getId())
                .name(workbookDto.getName())
                .problems(workbookService.getProblemsInWorkbook(id))
                .build();
        return ResponseEntity.ok(response);
    }


    /*
    * json {
    *       "name" : "이름"
    *       "problemList" : []
    * }
    */
    @PostMapping
    public ResponseEntity<?> createWorkbook(@RequestBody RequestSaveWorkbookDto requestSaveWorkbookDto) {
        workbookService.addWorkbook(requestSaveWorkbookDto);
        return ResponseEntity.ok(requestSaveWorkbookDto);
    }

    @PutMapping
    public ResponseEntity<?> updateWorkbook(@RequestBody RequestUpdateWorkbookDto requestUpdateWorkbookDto) {
        workbookService.updateWorkbook(requestUpdateWorkbookDto);
        return ResponseEntity.ok("success");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkbook(@PathVariable Long id) {
        return ResponseEntity.ok(workbookService.deleteWorkbook(id));
    }
}
