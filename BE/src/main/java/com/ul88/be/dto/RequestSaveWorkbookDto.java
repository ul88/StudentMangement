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
public class RequestSaveWorkbookDto {
    private String name;
    private List<Integer> problemList = new ArrayList<>();
}
