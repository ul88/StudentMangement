package com.ul88.be.repository;

import com.ul88.be.entity.Problem;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcProblemRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<Problem> problems){
        String sql = "INSERT IGNORE INTO problem(id, name, level) VALUES (?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, problems.stream().map(p->
                new Object[]{p.getId(), p.getName(), p.getLevel().name()}).toList());
    }
}
