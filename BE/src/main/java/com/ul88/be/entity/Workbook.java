package com.ul88.be.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Workbook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @OneToMany
    @JoinColumn(name = "workbook_id")
    private List<Problem> problems;
}
