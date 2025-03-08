package com.ul88.be.dto;

import com.ul88.be.entity.Student;
import lombok.*;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    private Long id;
    private String name;
    private String bojId;
    private String birth;

    public static StudentDto fromEntity(Student student) {
        return StudentDto.builder()
                .id(student.getId())
                .name(student.getName())
                .bojId(student.getBojId())
                .birth(student.getBirth())
                .build();
    }

    public Student toEntity() {
        return Student.builder()
                .id(id)
                .name(name)
                .bojId(bojId)
                .birth(birth)
                .build();
    }
}
