package com.sprint.mission.discodeit.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    //서비스
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"유저를 찾을 수 없습니다."),
    DUPLICATE_USER_NAME(HttpStatus.CONFLICT,"사용중인 이름입니다."),
    DUPLICATE_USER_EMAIL(HttpStatus.CONFLICT,"사용중인 이메일입니다."),

    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND,"채널을 찾을 수 없습니다."),
    PRIVATE_CHANNEL_UPDATE(HttpStatus.UNPROCESSABLE_CONTENT,"비공개 채널은 업데이트할 수 없습니다."),

    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND,"메세지를 찾을 수 없습니다."),

    LOGIN_FAILED_USERNAME(HttpStatus.UNAUTHORIZED,"로그인 실패 계정이 없습니다."),
    LOGIN_FAILED_PASSWORD(HttpStatus.UNAUTHORIZED,"로그인 실패 비밀번호가 일치하지 않습니다."),

    USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND,"유저정보를 찾을 수 없습니다."),

    READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND,"ReadStatus를 찾을 수 없습니다."),
    DUPLICATE_READ_STATUS(HttpStatus.CONFLICT,"해당 채널, 유저의 ReadStatus가 이미 존재합니다."),

    BINARY_NOT_FOUND(HttpStatus.NOT_FOUND,"파일정보를 찾을 수 없습니다."),

    ///////////////////////////////////////////////////////////////////////////////
    //스토리지
    STORAGE_INIT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"스토리지 초기화를 실패했습니다."),
    FILE_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일저장에 실패했습니다."),
    FILE_LOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 불러오기 실패했습니다."),
    ////////////////////////////////////////////////////////////////////
    //컨트롤러
    //todo 컨트롤러에 파일 io관련 예외 있음
    BINARY_REQUEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일을 Dto로 전환하는데 실패했습니다.")

    ;



    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status,  String message) {
        this.status = status;
        this.message = message;
    }

}
