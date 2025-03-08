package com.ul88.be.service;

import com.ul88.be.dto.ProblemDto;
import com.ul88.be.dto.StudentDto;
import com.ul88.be.entity.Management;
import com.ul88.be.entity.Problem;
import com.ul88.be.repository.ManagementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ManagementService {
    private final ManagementRepository managementRepository;
    private final StudentService studentService;
    private final ProblemService problemService;

    //Student Entity의 고유 번호를 사용
    @Transactional
    public void renewStudent(Long id) throws IOException {
        StudentDto studentDto = studentService.getStudent(id);
        checkSolved(studentDto);
    }

    public List<ProblemDto> getSolvedProblemsByStudent(Long id) {
        StudentDto studentDto = studentService.getStudent(id);

        List<Management> solvedList = managementRepository.findByStudent(studentDto.toEntity());

        return solvedList.stream().map(solved ->
                        ProblemDto.fromEntity(solved.getProblem()))
                .toList();
    }

    private void checkSolved(StudentDto student) throws IOException {
        List<Problem> problemList = problemService.parsingSolvedAc(student.getBojId());

        managementRepository.saveAll(problemList
                .stream().map(problem ->
                        Management.builder()
                                .student(student.toEntity())
                                .problem(problem)
                                .build()
                ).toList());
    }
}
