package com.ul88.be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseSolvedStudents {
    private ProblemDto problem;
    @Builder.Default
    private List<StudentDto> studentList = new ArrayList<>();

    public void addStudent(StudentDto dto){
        studentList.add(dto);
    }
}
