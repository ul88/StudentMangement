package com.ul88.be.dto;

import com.ul88.be.entity.ProblemLevel;
import lombok.Data;

@Data
public class SolvedAcDto {
    private Integer problemId;
    private String titleKo;
    private Integer level;

    public ProblemDto toProblem(){
        return ProblemDto.builder()
                .id(problemId)
                .name(titleKo)
                .level(ProblemLevel.fromNumber(level))
                .build();
    }
}
