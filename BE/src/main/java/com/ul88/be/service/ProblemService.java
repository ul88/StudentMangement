package com.ul88.be.service;

import com.ul88.be.dto.ProblemDto;
import com.ul88.be.dto.SolvedAcApiDto;
import com.ul88.be.dto.SolvedAcDto;
import com.ul88.be.dto.StudentDto;
import com.ul88.be.entity.Management;
import com.ul88.be.entity.Problem;
import com.ul88.be.entity.ProblemLevel;
import com.ul88.be.entity.Student;
import com.ul88.be.exception.CustomException;
import com.ul88.be.exception.ErrorCode;
import com.ul88.be.repository.JdbcProblemRepository;
import com.ul88.be.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProblemService {
    final String prefixUrl = "https://solved.ac/api/v3/search/problem?query=s@";
    final String suffixUrl = "&page=";

    private final ProblemRepository problemRepository;
    private final JdbcProblemRepository jdbcProblemRepository;

    public List<ProblemDto> getProblems(List<Integer> problemIds){
        return problemIds.stream().map(id -> {
            Optional<Problem> p = problemRepository.findById(id);
            if(p.isPresent()){
                return ProblemDto.fromEntity(p.get());
            }
            return addProblem(id);
        }).toList();
    }

    public List<StudentDto> getStudents(Integer id){
        return problemRepository.findStudentsByProblem(id).stream()
                .map(StudentDto::fromEntity).toList();
    }

    public List<Problem> parsingSolvedAc(String bojId) throws IOException {
        String url = prefixUrl + bojId;

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(Duration.ofSeconds(30));

        RestClient restClient = RestClient.builder()
                .requestFactory(requestFactory).build();

        SolvedAcApiDto responseApiDataList = restClient.get().uri(url)
                .header("x-solvedac-language", "ko")
                .retrieve()
                .body(SolvedAcApiDto.class);

        int pages = responseApiDataList.getCount()/50;
        if(responseApiDataList.getCount() % 50 != 0) pages++;

        List<Problem> problemList = new ArrayList<>();
        for(SolvedAcDto dto : responseApiDataList.getItems()){
            problemList.add(dto.toProblem().toEntity());
        }

        for(int i=2;i<=pages;i++){
            responseApiDataList = restClient.get().uri(url+suffixUrl + i)
                    .header("x-solvedac-language", "ko")
                    .retrieve()
                    .body(SolvedAcApiDto.class);

            for(SolvedAcDto dto : responseApiDataList.getItems()){
                Problem p = dto.toProblem().toEntity();
                problemList.add(existsProblem(p));
            }
        }

        jdbcProblemRepository.saveAll(problemList);

        return problemList;
    }

    private ProblemDto addProblem(Integer id){
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(Duration.ofSeconds(30));

        RestClient restClient = RestClient.builder()
                .requestFactory(requestFactory).build();

        String url = "https://solved.ac/api/v3/problem/show?problemId=" + id;
        SolvedAcDto solvedAcDto = restClient.get().uri(url)
                .header("x-solvedac-language", "ko")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    throw new CustomException(ErrorCode.PROBLEM_NOT_FOUND);
                }))
                .body(SolvedAcDto.class);

        Problem p = solvedAcDto.toProblem().toEntity();
        problemRepository.save(existsProblem(p));
        return ProblemDto.fromEntity(p);
    }

    private Problem existsProblem(Problem problem){
        return problemRepository.findById(problem.getId()).orElse(problem);
    }
}
