package com.ul88.be.repository;

import com.ul88.be.entity.Problem;
import com.ul88.be.entity.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByBojId(String bojId);

    @Query("select m.problem from Student s join s.managements m where s.id = :id")
    List<Problem> findByIdWithProblems(Long id);
}
