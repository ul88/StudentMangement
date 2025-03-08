package com.ul88.be.repository;

import com.ul88.be.entity.Management;
import com.ul88.be.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManagementRepository extends JpaRepository<Management, Long> {
    List<Management> findByStudent(Student student);
}
