package com.ul88.be.repository;

import com.ul88.be.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Integer> {
    @Query(value = "SELECT * FROM problem WHERE student_id = :id"
            ,nativeQuery = true)
    List<Problem> findByStudentId(Long id);

    @Query(value = "SELECT * FROM problem WHERE workbook_id = :id"
            ,nativeQuery = true)
    List<Problem> findByWorkbookId(Long id);
}
