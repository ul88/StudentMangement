package com.ul88.be.dto;

import com.ul88.be.entity.Workbook;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemInWorkbookDto {
    private Long id;
    private ProblemDto problem;
    private WorkbookDto workbook;
}
