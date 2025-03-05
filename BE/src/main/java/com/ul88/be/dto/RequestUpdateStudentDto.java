package com.ul88.be.dto;

import com.ul88.be.entity.Student;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdateStudentDto {
    private Long id;
    private String name;
    private String bojId;
    private String birth;
    private List<ProblemDto> problems = new ArrayList<>();
}
