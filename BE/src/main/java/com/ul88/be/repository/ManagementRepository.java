package com.ul88.be.repository;

import com.ul88.be.entity.Management;
import com.ul88.be.entity.Problem;
import com.ul88.be.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ManagementRepository extends JpaRepository<Management, Long> {
    @Query("select m.student from Management m where m.student.id in :students")
    List<Student> findByStudentInAndProblem(List<Long> students, Problem problem);

    @Query("select distinct m.student from Management m JOIN m.problem p where p.id = :problemId")
    List<Student> findStudentsByProblem(Integer problemId);

    boolean existsByStudentAndProblem(Student student, Problem problem);
}
