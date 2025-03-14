package com.ul88.be;

import com.ul88.be.entity.Management;
import com.ul88.be.entity.Problem;
import com.ul88.be.entity.ProblemLevel;
import com.ul88.be.entity.Student;
import com.ul88.be.repository.ManagementRepository;
import com.ul88.be.repository.ProblemRepository;
import com.ul88.be.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ManagementTest {
    @Autowired
    private ManagementRepository managementRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Test
    @Transactional
    public void searchTest(){

        studentRepository.save(Student.builder()
                        .name("김한울")
                        .bojId("force0467")
                        .birth("040830")
                .build());

        studentRepository.save(Student.builder()
                .name("김한울1")
                .bojId("aca1234")
                .birth("040830")
                .build());

        studentRepository.save(Student.builder()
                .name("김한울2")
                .bojId("aca12")
                .birth("040830")
                .build());

        problemRepository.save(Problem.builder()
                        .id(1001)
                        .name("A+B")
                        .level(ProblemLevel.Bronze1)
                .build());

        managementRepository.save(Management.builder()
                        .student(studentRepository.findById(1L).get())
                        .problem(problemRepository.findById(1001).get())
                .build());

        managementRepository.save(Management.builder()
                .student(studentRepository.findById(2L).get())
                .problem(problemRepository.findById(1001).get())
                .build());
        managementRepository.save(Management.builder()
                .student(studentRepository.findById(3L).get())
                .problem(problemRepository.findById(1001).get())
                .build());

        List<Long> students = List.of(1L, 2L);


    }
}
