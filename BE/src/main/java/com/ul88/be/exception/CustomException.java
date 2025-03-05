package com.ul88.be.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
}
