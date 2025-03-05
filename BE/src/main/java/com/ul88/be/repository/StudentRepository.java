package com.ul88.be.repository;

import com.ul88.be.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByBojId(String bojId);
    Optional<Student> findByName(String name);
}
