package com.ul88.be.controller;

import com.ul88.be.dto.RequestSaveStudentDto;
import com.ul88.be.dto.StudentDto;
import com.ul88.be.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<?> getStudents() {
        return ResponseEntity.ok(studentService.getStudent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudent(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(studentService.getStudent(id));
    }

    @PostMapping // 입력
    public ResponseEntity<?> saveStudent(@RequestBody RequestSaveStudentDto studentDto) {
        studentService.saveStudent(studentDto);
        return ResponseEntity.ok().body("success");
    }

    @PutMapping
    public ResponseEntity<?> updateProblems(@RequestBody StudentDto studentDto) {
        studentService.updateStudent(studentDto);
        return ResponseEntity.ok().body("update");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok("delete success");
    }
}
