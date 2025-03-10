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
@EqualsAndHashCode
@ToString
public class Problem {
    @Id
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private ProblemLevel level = ProblemLevel.Unrated;

    @OneToMany(mappedBy = "problem",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Management> managementList = new ArrayList<>();
}
