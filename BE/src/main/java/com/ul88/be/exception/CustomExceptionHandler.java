package com.ul88.be.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ExceptionDto> handleCustomException(CustomException e) {
        return ExceptionDto.toResponseEntity(e.getErrorCode());
    }
}
