package com.ul88.be.controller;

import com.ul88.be.dto.RequestSaveStudentDto;
import com.ul88.be.dto.RequestUpdateStudentDto;
import com.ul88.be.dto.ResponseStudentDto;
import com.ul88.be.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService studentService;

    @PostMapping // 입력
    public ResponseEntity<RequestSaveStudentDto> saveStudent(@RequestBody RequestSaveStudentDto studentDto) {
        studentService.saveStudent(studentDto);
        return ResponseEntity.ok().body(studentDto);
    }

    @PutMapping
    public ResponseEntity<?> updateProblems(@RequestBody RequestUpdateStudentDto studentDto) {
        studentService.updateStudent(studentDto);
        return ResponseEntity.ok().body("update");
    }

    @GetMapping
    public ResponseEntity<?> getStudents() {
        return ResponseEntity.ok(studentService.getStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudent(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(studentService.getStudent(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok("delete success");
    }
}
