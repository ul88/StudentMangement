package com.ul88.be.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString(exclude = {"managements"})
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name="boj_id", unique = true, nullable = false)
    private String bojId;

    @OneToMany(mappedBy = "student")
    private List<Management> managements;

    private String birth;
    public void changeName(String name) {
        this.name = name;
    }

    public void changeBojId(String bojId) {
        this.bojId = bojId;
    }

    public void changeBirth(String birth) {
        this.birth = birth;
    }
}
