package com.ul88.be.dto;

import com.ul88.be.entity.Workbook;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdateWorkbookDto {
    private Long id;
    private String name;
    private List<ProblemDto> problems;

    public Workbook toEntity(){
        return Workbook.builder()
                .id(id)
                .name(name)
                .problems(problems.stream().map(ProblemDto::toEntity).collect(Collectors.toList()))
                .build();
    }

    public static RequestUpdateWorkbookDto fromEntity(Workbook dto) {
        return RequestUpdateWorkbookDto.builder()
                .id(dto.getId())
                .name(dto.getName())
                .problems(dto.getProblems().stream().map(ProblemDto::fromEntity).collect(Collectors.toList()))
                .build();
    }
}
