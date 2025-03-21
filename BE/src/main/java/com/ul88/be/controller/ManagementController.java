package com.ul88.be.controller;

import com.ul88.be.service.ManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/management")
@RequiredArgsConstructor
@Log4j2
public class ManagementController {
    private final ManagementService managementService;

    @GetMapping("")
    public ResponseEntity<?> get(@RequestParam("workbookId") Long workbookId,
                                 @RequestParam(value = "studentId", required = false) List<Long> studentIdList) {
        if(studentIdList == null){
            return ResponseEntity.ok(
                    managementService.getProblemListSolvedInWorkbookCheckAll(workbookId)
            );
        }else{
            return ResponseEntity.ok(
                    managementService.getProblemListSolvedInWorkbookCheckStudents(workbookId, studentIdList)
            );
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> renew(@PathVariable("id") Long id) throws IOException {
        managementService.renewStudent(id);
        return ResponseEntity.ok("success");
    }
}
