package com.ul88.be.dto;

import com.ul88.be.entity.Problem;
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
public class WorkbookDto {
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

    public static WorkbookDto fromEntity(Workbook dto) {
        return WorkbookDto.builder()
                .id(dto.getId())
                .name(dto.getName())
                .problems(dto.getProblems().stream().map(ProblemDto::fromEntity).collect(Collectors.toList()))
                .build();
    }
}
