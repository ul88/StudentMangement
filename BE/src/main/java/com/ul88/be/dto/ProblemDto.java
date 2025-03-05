package com.ul88.be.dto;

import com.ul88.be.entity.Problem;
import com.ul88.be.entity.ProblemLevel;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemDto {
    private Integer id;
    private String name;
    @Enumerated(EnumType.STRING)
    private ProblemLevel level;

    public Problem toEntity(){
        return Problem.builder()
                .id(this.id)
                .name(this.name)
                .level(this.level)
                .build();
    }

    public static ProblemDto fromEntity(Problem entity){
        return ProblemDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .level(entity.getLevel())
                .build();
    }
}
