package com.ul88.be.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Workbook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "workbook",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @Builder.Default
    private List<ProblemInWorkbook> problems = new ArrayList<>();

    public void changeName(String name) {
        this.name = name;
    }

    public void addProblem(Problem problem) {
        problems.add(ProblemInWorkbook.builder()
                .workbook(this)
                .problem(problem)
                .build());
    }
}
