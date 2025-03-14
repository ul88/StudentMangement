package com.ul88.be.service;

import com.ul88.be.dto.ProblemDto;
import com.ul88.be.dto.ResponseSolvedStudents;
import com.ul88.be.dto.StudentDto;
import com.ul88.be.entity.Management;
import com.ul88.be.entity.Problem;
import com.ul88.be.entity.Student;
import com.ul88.be.repository.JdbcManagementRepository;
import com.ul88.be.repository.ManagementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class ManagementService {
    private final JdbcManagementRepository jdbcManagementRepository;
    private final ManagementRepository managementRepository;
    private final StudentService studentService;
    private final ProblemService problemService;
    private final WorkbookService workbookService;

    //Student Entity의 고유 번호를 사용
    @Transactional
    public void renewStudent(Long id) throws IOException {
        StudentDto studentDto = studentService.getStudent(id);
        checkSolved(studentDto);
    }

    public List<ResponseSolvedStudents> getProblemListSolvedInWorkbookCheckAll(Long workbookId) {
        List<ProblemDto> problemList = workbookService.getProblemsInWorkbook(workbookId);

        List<ResponseSolvedStudents> response = new ArrayList<>();
        for(ProblemDto p : problemList) {
            List<StudentDto> studentList = problemService.getStudents(p.getId());
            response.add(ResponseSolvedStudents.builder()
                            .problem(p)
                            .studentList(studentList)
                    .build());
        }
        return response;
    }

    public List<ResponseSolvedStudents> getProblemListSolvedInWorkbookCheckStudents(
            Long workbookId,
            List<Long> studentIdList
    ){
        List<ProblemDto> problemList = workbookService.getProblemsInWorkbook(workbookId);

        List<ResponseSolvedStudents> response = new ArrayList<>();
        for(ProblemDto p : problemList) {
            List<StudentDto> studentList =
                    managementRepository.findByStudentInAndProblem(studentIdList, p.toEntity()).stream()
                            .map(StudentDto::fromEntity).toList();
            response.add(ResponseSolvedStudents.builder()
                    .problem(p)
                    .studentList(studentList)
                    .build());
        }
        return response;
    }

    private void checkSolved(StudentDto studentDto) throws IOException {
        Student student = studentDto.toEntity();
        List<Problem> problemList = problemService.parsingSolvedAc(student.getBojId());

        List<Management> managementList = new ArrayList<>();

        for(Problem problem : problemList) {
            if(!managementRepository.existsByStudentAndProblem(student, problem)) {
                managementList.add(Management.builder()
                        .student(student)
                        .problem(problem)
                        .build());
            }
        }

        jdbcManagementRepository.saveAll(managementList);
    }
}
