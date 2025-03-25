package com.ul88.be.service;

import com.ul88.be.dto.ProblemDto;
import com.ul88.be.dto.RequestSaveWorkbookDto;
import com.ul88.be.dto.RequestUpdateWorkbookDto;
import com.ul88.be.dto.WorkbookDto;
import com.ul88.be.entity.Problem;
import com.ul88.be.entity.Workbook;
import com.ul88.be.exception.CustomException;
import com.ul88.be.exception.ErrorCode;
import com.ul88.be.repository.WorkbookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class WorkbookService {
    private final WorkbookRepository workbookRepository;
    private final ProblemService problemService;

    public void addWorkbook(RequestSaveWorkbookDto requestSaveWorkbookDto){

        Workbook workbook = Workbook.builder()
                .name(requestSaveWorkbookDto.getName())
                .build();

        if(!requestSaveWorkbookDto.getProblemList().isEmpty()){
            problemService.getProblems(requestSaveWorkbookDto.getProblemList()).forEach(problem -> {
                workbook.addProblem(problem.toEntity());
            });
        }

        workbookRepository.save(workbook);
    }

    @Transactional
    public void updateWorkbook(RequestUpdateWorkbookDto requestUpdateWorkbookDto){
        Workbook entity = workbookRepository.findById(requestUpdateWorkbookDto.getId()).orElseThrow(() ->
                new CustomException(ErrorCode.PK_NOT_FOUND));
        if(requestUpdateWorkbookDto.getName() != null){
            entity.changeName(requestUpdateWorkbookDto.getName());
        }
        problemService.getProblems(requestUpdateWorkbookDto.getProblemList()).forEach(problem -> {
            entity.addProblem(problem.toEntity());
        });

        workbookRepository.save(entity);
    }

    public List<WorkbookDto> getWorkbooks(){
        return workbookRepository.findAll().stream()
                .map(WorkbookDto::fromEntity).toList();
    }

    public WorkbookDto getWorkbook(Long id){
        Workbook workbook = workbookRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.PK_NOT_FOUND));
        return WorkbookDto.fromEntity(workbook);
    }

    public List<ProblemDto> getProblemsInWorkbook(Long id){
        List<Problem> problemList = workbookRepository.findProblemByWorkbook(id);
        return problemList.stream().map(ProblemDto::fromEntity).toList();
    }

    public WorkbookDto deleteWorkbook(Long id){
        Workbook workbook = workbookRepository.findById(id).orElseThrow(()->
                new CustomException(ErrorCode.PK_NOT_FOUND));
        workbookRepository.delete(workbook);
        return WorkbookDto.fromEntity(workbook);
    }
}
