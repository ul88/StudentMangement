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
public class ResponseWorkbookDto {
    private Long id;
    private String name;

    public static ResponseWorkbookDto fromEntity(Workbook entity){
        return ResponseWorkbookDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
