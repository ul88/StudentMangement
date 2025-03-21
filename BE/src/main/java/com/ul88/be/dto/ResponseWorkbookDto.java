package com.ul88.be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseWorkbookDto {
    private Long id;
    private String name;
    private List<ProblemDto> problems;
}
