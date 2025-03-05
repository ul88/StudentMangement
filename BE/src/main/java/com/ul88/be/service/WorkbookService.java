package com.ul88.be.service;

import com.ul88.be.dto.ProblemDto;
import com.ul88.be.dto.WorkbookDto;
import com.ul88.be.entity.Workbook;
import com.ul88.be.exception.CustomException;
import com.ul88.be.exception.ErrorCode;
import com.ul88.be.repository.WorkbookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class WorkbookService {
    private final WorkbookRepository workbookRepository;
    private final ProblemService problemService;


    public void equalWorkbookAndProblem(){

    }

    public void addWorkbook(String name){
        workbookRepository.save(Workbook.builder()
                        .name(name)
                .build());
    }

    @Transactional
    public void updateWorkbook(WorkbookDto workbookDto){
        Workbook entity = workbookRepository.findById(workbookDto.getId()).orElseThrow(() ->
                new CustomException(ErrorCode.PK_NOT_FOUND));

        if(workbookDto.getName() != null){
            entity.changeName(workbookDto.getName());
        }
        if(!workbookDto.getProblems().isEmpty()){
            for(ProblemDto problem : workbookDto.getProblems()){
                entity.addProblem(problemService.getProblem(problem.getId()).toEntity());
            }
        }

        workbookRepository.save(entity);
    }

    public List<WorkbookDto> getWorkbooks(){
        return workbookRepository.findAll().stream()
                .map(WorkbookDto::fromEntity).collect(Collectors.toList());
    }

    public List<ProblemDto> getWorkbookInProblems(Long id){
        return problemService.getWorkbookInProblems(id);
    }
}
