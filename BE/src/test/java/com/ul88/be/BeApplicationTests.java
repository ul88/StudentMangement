package com.ul88.be;

import com.ul88.be.dto.ProblemDto;
import com.ul88.be.dto.RequestSaveStudentDto;
import com.ul88.be.dto.WorkbookDto;
import com.ul88.be.service.ManagementService;
import com.ul88.be.service.ProblemService;
import com.ul88.be.service.StudentService;
import com.ul88.be.service.WorkbookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
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
    private WorkbookService workbookService;

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
        System.out.println(problemService.getStudentInProblems(1L));
    }

    @Test
    void addWorkbookAndAddProblem(){
        List<ProblemDto> problemList = new ArrayList<>();
        problemList.add(problemService.getProblem(1000));
        problemList.add(problemService.getProblem(1001));

        /*workbookService.addWorkbook("문제집1");*/
        workbookService.updateWorkbook(WorkbookDto.builder()
                        .id(1L)
                        .name("문제집1")
                        .problems(problemList)
                .build());
    }
}
