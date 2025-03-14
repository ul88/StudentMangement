package com.ul88.be.repository;

import com.ul88.be.entity.Management;
import com.ul88.be.entity.Problem;
import com.ul88.be.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ProblemRepository extends JpaRepository<Problem, Integer> {
    @Query("select m.student from Problem p join p.managementList m where p.id = :problemId")
    List<Student> findStudentsByProblem(Integer problemId);
}
