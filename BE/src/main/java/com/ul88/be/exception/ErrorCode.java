package com.ul88.be.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    PK_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 기본키를 요청했습니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "사용자가 이미 존재합니다."),
    BOJ_ID_NOT_EXISTS(HttpStatus.BAD_REQUEST, "백준 아이디를 입력 받지 않았습니다."),
    PROBLEM_NOT_FOUND(HttpStatus.BAD_REQUEST, "문제를 찾을 수 없습니다."),
    PROBLEM_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 문제입니다.");

    private final HttpStatus httpStatus;
    private final String msg;
}
