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

    @OneToMany
    @Builder.Default
    @JoinColumn(name = "workbook_id")
    private List<Problem> problems = new ArrayList<>();

    public void changeName(String name) {
        this.name = name;
    }

    public void addProblem(Problem problem) {
        problems.add(problem);
    }
}
