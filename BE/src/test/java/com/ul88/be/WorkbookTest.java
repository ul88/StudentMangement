package com.ul88.be;

import com.ul88.be.dto.RequestUpdateWorkbookDto;
import com.ul88.be.entity.Problem;
import com.ul88.be.entity.ProblemInWorkbook;
import com.ul88.be.entity.Workbook;
import com.ul88.be.repository.WorkbookRepository;
import com.ul88.be.service.WorkbookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class WorkbookTest {
    @Autowired
    private WorkbookService workbookService;

    @Autowired
    private WorkbookRepository workbookRepository;

    @Test
    public void searchWorkbook(){
        List<Problem> p = workbookRepository.findProblemByWorkbook(1L);
        for(Problem entity : p){
            System.out.println(entity.getName());
        }

        Workbook workbook = workbookRepository.findById(1L).orElse(null);
        List<Problem> problems = workbook.getProblems().stream().map(ProblemInWorkbook::getProblem).toList();
        for(Problem e : problems){
            System.out.println(e.getName());
        }
    }
}
