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
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name="boj_id", unique = true, nullable = false)
    private String bojId;

    private String birth;

    @OneToMany
    @Builder.Default
    @JoinColumn(name = "student_id")
    private List<Problem> problems = new ArrayList<>();

    public void changeName(String name) {
        this.name = name;
    }

    public void changeBojId(String bojId) {
        this.bojId = bojId;
    }

    public void changeBirth(String birth) {
        this.birth = birth;
    }

    public void addProblem(Problem problem) {
        problems.add(problem);
    }

    public void removeProblem(Problem problem) {
        problems.remove(problem);
    }
}
