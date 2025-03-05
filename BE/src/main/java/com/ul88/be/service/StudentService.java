package com.ul88.be.service;

import com.ul88.be.dto.ProblemDto;
import com.ul88.be.dto.RequestSaveStudentDto;
import com.ul88.be.dto.RequestUpdateStudentDto;
import com.ul88.be.dto.ResponseStudentDto;
import com.ul88.be.entity.Student;
import com.ul88.be.exception.CustomException;
import com.ul88.be.exception.ErrorCode;
import com.ul88.be.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class StudentService {
    private final StudentRepository studentRepository;

    public void saveStudent(RequestSaveStudentDto dto) {
        if(dto.getBojId() == null) {
            throw new CustomException(ErrorCode.BOJ_ID_NOT_EXISTS);
        }
        studentRepository.findByBojId(dto.getBojId()).ifPresent(i -> {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        });

        studentRepository.save(dto.toEntity());
    }

    public void updateStudent(RequestUpdateStudentDto dto) {
        Student entity = studentRepository.findById(dto.getId()).orElseThrow(() ->
                new CustomException(ErrorCode.USER_NOT_FOUND));

        if(dto.getName() != null) entity.changeName(dto.getName());
        if(dto.getBojId() != null) entity.changeBojId(dto.getBojId());
        if(dto.getBirth() != null) entity.changeBirth(dto.getBirth());

        if(!dto.getProblems().isEmpty()) {
            if(entity.getBojId() == null) {
                throw new CustomException(ErrorCode.BOJ_ID_NOT_EXISTS);
            }
            for(ProblemDto p : dto.getProblems()) {
                entity.addProblem(p.toEntity());
            }
        }

        studentRepository.save(entity);
    }

    @Transactional
    public List<ResponseStudentDto> getStudents(){
        return studentRepository.findAll().stream().map(ResponseStudentDto::fromEntity).collect(Collectors.toList());
    }

    @Transactional
    public ResponseStudentDto getStudent(Long id){
        Student entity = studentRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.PK_NOT_FOUND));

        return ResponseStudentDto.fromEntity(entity);
    }

    @Transactional
    public ResponseStudentDto getStudent(String name){
        Student entity = studentRepository.findByName(name).orElseThrow(() ->
                new CustomException(ErrorCode.USER_NOT_FOUND));
        return ResponseStudentDto.fromEntity(entity);
    }

    public void deleteStudent(Long id){
        studentRepository.delete(studentRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.PK_NOT_FOUND)));
    }
}
