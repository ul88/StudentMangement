package com.ul88.be.service;

import com.ul88.be.dto.ProblemDto;
import com.ul88.be.dto.SolvedAcApiDto;
import com.ul88.be.dto.SolvedAcDto;
import com.ul88.be.entity.Problem;
import com.ul88.be.entity.ProblemLevel;
import com.ul88.be.exception.CustomException;
import com.ul88.be.exception.ErrorCode;
import com.ul88.be.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProblemService {
    final String prefixUrl = "https://solved.ac/api/v3/search/problem?query=s@";
    final String suffixUrl = "&page=";

    private final ProblemRepository problemRepository;

    public void addProblem(Integer id) {
        problemRepository.findById(id).ifPresent(i -> {
            throw new CustomException(ErrorCode.PROBLEM_ALREADY_EXISTS);
        });

        problemRepository.save(checkEXISTSProblem(id));
    }

    public void deleteProblem(Integer id) {
        Problem entity = problemRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.PROBLEM_NOT_FOUND));

        problemRepository.delete(entity);
    }

    public ProblemDto getProblem(Integer id){
        Optional<Problem> entity = problemRepository.findById(id);
        if(entity.isEmpty()){
            addProblem(id);
            return getProblem(id);
        }

        return ProblemDto.fromEntity(entity.get());
    }

    public List<ProblemDto> getStudents(){
        return problemRepository.findAll().stream()
                .map(ProblemDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ProblemDto> getProblemsInStudent(Long id){
        return problemRepository.findByStudentId(id).stream()
                .map(ProblemDto::fromEntity).collect(Collectors.toList());
    }

    public List<ProblemDto> getProblemsInWorkbook(Long id){
        return problemRepository.findByWorkbookId(id).stream()
                .map(ProblemDto::fromEntity).collect(Collectors.toList());
    }

    public List<ProblemDto> getStudentSolvedProblemsInWorkbook(Long studentId, Long workbookId){
        return problemRepository.findByStudentIdAndWorkbookId(studentId,workbookId).stream()
                .map(ProblemDto::fromEntity).collect(Collectors.toList());
    }

    public List<ProblemDto> parsingSolvedAc(String bojId) throws IOException {
        String url = prefixUrl + bojId;

        RestClient restClient = RestClient.create();
        SolvedAcApiDto responseApiDataList = restClient.get().uri(url)
                .header("x-solvedac-language", "ko")
                .retrieve()
                .body(SolvedAcApiDto.class);

        int pages = responseApiDataList.getCount()/50;
        if(responseApiDataList.getCount() % 50 != 0) pages++;

        List<Problem> problemList = new ArrayList<>();
        for(SolvedAcDto dto : responseApiDataList.getItems()){
            problemList.add(SolvedAcToProblem(dto));
        }

        for(int i=2;i<=pages;i++){
            responseApiDataList = restClient.get().uri(url+suffixUrl + i)
                    .header("x-solvedac-language", "ko")
                    .retrieve()
                    .body(SolvedAcApiDto.class);

            for(SolvedAcDto dto : responseApiDataList.getItems()){
                problemList.add(SolvedAcToProblem(dto));
            }
        }

        problemRepository.saveAll(problemList);

        return problemList.stream().map(ProblemDto::fromEntity).collect(Collectors.toList());
    }

    private Problem checkEXISTSProblem(Integer id){
        RestClient restClient = RestClient.create();
        String url = "https://solved.ac/api/v3/problem/show?problemId" + id;
        SolvedAcDto solvedAcDto = restClient.get().uri(url)
                .header("x-solvedac-language", "ko")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    throw new CustomException(ErrorCode.PROBLEM_NOT_FOUND);
                }))
                .body(SolvedAcDto.class);


        return SolvedAcToProblem(solvedAcDto);
    }

    private Problem SolvedAcToProblem(SolvedAcDto solved){
        return Problem.builder()
                .id(solved.getProblemId())
                .name(solved.getTitleKo())
                .level(ProblemLevel.fromNumber(solved.getLevel()))
                .build();
    }
}
