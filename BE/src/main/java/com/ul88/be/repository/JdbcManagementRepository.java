package com.ul88.be.repository;

import com.ul88.be.entity.Management;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcManagementRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<Management> managements) {
        String sql = "INSERT IGNORE INTO management(student_id, problem_id) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(sql, managements.stream().map(m ->
                new Object[]{m.getStudent().getId(), m.getProblem().getId()}
        ).toList());
    }
}
