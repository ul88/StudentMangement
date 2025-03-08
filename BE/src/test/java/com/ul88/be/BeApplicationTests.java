package com.ul88.be;

import com.ul88.be.dto.ProblemDto;
import com.ul88.be.dto.RequestSaveStudentDto;
import com.ul88.be.dto.RequestUpdateWorkbookDto;
import com.ul88.be.entity.Problem;
import com.ul88.be.entity.ProblemLevel;
import com.ul88.be.entity.Management;
import com.ul88.be.entity.Student;
import com.ul88.be.repository.ProblemRepository;
import com.ul88.be.repository.ManagementRepository;
import com.ul88.be.repository.StudentRepository;
import com.ul88.be.service.ManagementService;
import com.ul88.be.service.ProblemService;
import com.ul88.be.service.StudentService;
import com.ul88.be.service.WorkbookService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class BeApplicationTests {

    @Autowired
    private ManagementService managementService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private WorkbookService workbookService;
    @Autowired
    private ProblemRepository problemRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ManagementRepository solvedRepository;

    @Test
    void parsing() throws IOException {
        studentService.saveStudent(RequestSaveStudentDto.builder()
                        .name("김한울2")
                        .bojId("aca1234")
                        .birth("040830")
                .build());
        managementService.renewStudent(2L);
    }


    @Test
    void findStudent(){
        System.out.println(studentService.getStudent(1L).getName());
    }

    @Test
    void findStudentProblems(){
        System.out.println(problemService.getProblemsInStudent(1L));
    }

    @Test
    void addWorkbookAndAddProblem(){
        List<ProblemDto> problemList = new ArrayList<>();
        problemList.add(problemService.getProblem(1000));
        problemList.add(problemService.getProblem(1001));

        /*workbookService.addWorkbook("문제집1");*/
        workbookService.updateWorkbook(RequestUpdateWorkbookDto.builder()
                        .id(1L)
                        .name("문제집1")
                        .problems(problemList)
                .build());
    }


    @Test
    void jwtTest(){
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");

        Claims claims = Jwts.claims();
        claims.put("userId", "admin");
        claims.put("email", "@gmail.com");
        claims.put("role", "admin");

        String test = Jwts.builder()
                .setHeader(header)

                .compact();
    }

    @Test
    void mappingTable(){
        Problem problem1 = Problem.builder()
                .id(1000)
                .name("A+B")
                .level(ProblemLevel.Bronze1)
                .build();
        problemRepository.save(problem1);
        Problem problem2 = Problem.builder()
                .id(1001)
                .name("A+B2")
                .level(ProblemLevel.Bronze1)
                .build();
        problemRepository.save(problem2);

        Student student = Student.builder()
                .name("김한울")
                .bojId("force0467")
                .birth("040830")
                .build();

        Student student1 = Student.builder()
                .name("김한울2")
                .bojId("aca1234")
                .birth("040830")
                .build();

        studentRepository.save(student);
        studentRepository.save(student1);
        solvedRepository.save(Management.builder()
                        .student(student)
                        .problem(problem1)
                .build());

        solvedRepository.save(Management.builder()
                        .student(student)
                        .problem(problem2)
                .build());
        solvedRepository.save(Management.builder()
                .student(student)
                .problem(problem2)
                .build());

        solvedRepository.save(Management.builder()
                        .student(student1)
                .problem(problem1)
                .build());

        studentRepository.deleteById(1L);
//        solvedRepository.deleteById(1L);
    }
}
