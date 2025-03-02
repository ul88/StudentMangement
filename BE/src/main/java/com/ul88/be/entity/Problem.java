package com.ul88.be.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Problem {
    @Id
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private ProblemLevel level = ProblemLevel.Unrated;
}
