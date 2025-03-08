package com.ul88.be.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
