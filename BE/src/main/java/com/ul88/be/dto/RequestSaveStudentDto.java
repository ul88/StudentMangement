package com.ul88.be.dto;

import com.ul88.be.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestSaveStudentDto {
    private Long id;
    private String name;
    private String bojId;
    private String birth;

    public Student toEntity(){
        return Student.builder()
                .id(id)
                .name(name)
                .bojId(bojId)
                .birth(birth)
                .build();
    }
}
