package com.ul88.be.service;

import com.ul88.be.dto.RequestSaveStudentDto;
import com.ul88.be.dto.StudentDto;
import com.ul88.be.entity.Student;
import com.ul88.be.exception.CustomException;
import com.ul88.be.exception.ErrorCode;
import com.ul88.be.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public void updateStudent(StudentDto dto) {
        Student entity = studentRepository.findById(dto.getId()).orElseThrow(() ->
                new CustomException(ErrorCode.USER_NOT_FOUND));

        if(dto.getName() != null) entity.changeName(dto.getName());
        if(dto.getBojId() != null) entity.changeBojId(dto.getBojId());
        if(dto.getBirth() != null) entity.changeBirth(dto.getBirth());
        studentRepository.save(entity);
    }

    @Transactional
    public List<StudentDto> getStudent(){
        return studentRepository.findAll().stream().map(StudentDto::fromEntity).toList();
    }

    @Transactional
    public StudentDto getStudent(Long id){
        Student entity = studentRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.PK_NOT_FOUND));

        return StudentDto.fromEntity(entity);
    }

    public void deleteStudent(Long id){
        studentRepository.delete(studentRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.PK_NOT_FOUND)));
    }

    public boolean existsStudent(Long id){
        return studentRepository.existsById(id);
    }
}
