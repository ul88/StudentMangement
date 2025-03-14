package com.ul88.be;

import com.ul88.be.entity.Problem;
import com.ul88.be.entity.ProblemLevel;
import com.ul88.be.entity.Management;
import com.ul88.be.entity.Student;
import com.ul88.be.repository.JdbcProblemRepository;
import com.ul88.be.repository.ProblemRepository;
import com.ul88.be.repository.ManagementRepository;
import com.ul88.be.repository.StudentRepository;
import com.ul88.be.service.ManagementService;
import com.ul88.be.service.ProblemService;
import com.ul88.be.service.StudentService;
import com.ul88.be.service.WorkbookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class BeApplicationTests {

    @Autowired
    private ManagementService managementService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private JdbcProblemRepository jdbcProblemRepository;

    @Autowired
    private WorkbookService workbookService;
    @Autowired
    private ProblemRepository problemRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ManagementRepository managementRepository;

    @Test
    void joinTest(){
        List<Student> m = managementRepository.findStudentsByProblem(1012);
        for(Student i : m){
            System.out.println(i.getName());
        }
    }

    @Test
    void findByproblemId(){
        Problem p = problemRepository.findById(1012).orElseThrow();
        for(Management m : p.getManagementList()){
            System.out.println(m.getStudent().getName() + " " + m.getProblem().getName());
        }
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
        managementRepository.save(Management.builder()
                .student(student)
                .problem(problem1)
                .build());

        managementRepository.save(Management.builder()
                .student(student)
                .problem(problem2)
                .build());
        managementRepository.save(Management.builder()
                .student(student)
                .problem(problem2)
                .build());

        managementRepository.save(Management.builder()
                .student(student1)
                .problem(problem1)
                .build());
    }

    @Test
    void jdbcSaveAllTest(){
        List<Problem> problems = new ArrayList<>();
        problems.add(Problem.builder()
                .id(1111)
                .name("A+B")
                .level(ProblemLevel.Bronze1)
                .build());
        problems.add(Problem.builder()
                .id(1112)
                .name("A+B1")
                .level(ProblemLevel.Bronze1)
                .build());

        jdbcProblemRepository.saveAll(problems);
    }
}
