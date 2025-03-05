package com.ul88.be.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Builder
@Getter
public class ExceptionDto {
    private int status;
    private String message;

    public static ResponseEntity<ExceptionDto> toResponseEntity(ErrorCode e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ExceptionDto.builder()
                        .status(e.getHttpStatus().value())
                        .message(e.getMsg())
                        .build());
    }
}
