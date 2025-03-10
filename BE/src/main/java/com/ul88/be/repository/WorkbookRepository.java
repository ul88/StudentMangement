package com.ul88.be.repository;

import com.ul88.be.entity.Problem;
import com.ul88.be.entity.ProblemInWorkbook;
import com.ul88.be.entity.Workbook;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkbookRepository extends JpaRepository<Workbook, Long> {
    @Query("select p.problem from Workbook w join w.problems p where w.id = :workbookId")
    @EntityGraph(attributePaths = {"problems"})
    List<Problem> findProblemByWorkbook(Long workbookId);
}
