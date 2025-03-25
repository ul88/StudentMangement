package com.ul88.be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseStudentDto{
    private String name;
    private String bojId;
    private String birth;
    private Integer problemCount;
    private List<ProblemDto> problemList;

    public static ResponseStudentDto fromEntity(StudentDto studentDto, List<ProblemDto> problemList){
        return ResponseStudentDto.builder()
                .name(studentDto.getName())
                .bojId(studentDto.getBojId())
                .birth(studentDto.getBirth())
                .problemCount(problemList.size())
                .problemList(problemList)
                .build();
    }
}
