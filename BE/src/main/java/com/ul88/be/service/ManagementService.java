package com.ul88.be.service;

import com.ul88.be.dto.ProblemDto;
import com.ul88.be.dto.RequestUpdateStudentDto;
import com.ul88.be.dto.ResponseStudentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ManagementService {

    private final StudentService studentService;
    private final ProblemService problemService;

    //Student Entity의 고유 번호를 사용
    @Transactional
    public void renewStudent(Long id) throws IOException {
        ResponseStudentDto studentDto = studentService.getStudent(id);
        List<ProblemDto> problemList = problemService.parsingSolvedAc(studentDto.getBojId());
        studentService.updateStudent(
                RequestUpdateStudentDto.builder()
                .id(studentDto.getId())
                .problems(problemList)
                .build()
        );
    }

}
