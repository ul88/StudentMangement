package com.ul88.be.dto;

import com.ul88.be.entity.Student;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseStudentDto {
    private Long id;
    private String name;
    private String bojId;
    private String birth;
    @Builder.Default
    private List<ProblemDto> problems = new ArrayList<>();

    public static ResponseStudentDto fromEntity(Student student) {
        return ResponseStudentDto.builder()
                .id(student.getId())
                .name(student.getName())
                .bojId(student.getBojId())
                .birth(student.getBirth())
                .problems(student.getProblems().stream().map(ProblemDto::fromEntity).collect(Collectors.toList()))
                .build();
    }
}
