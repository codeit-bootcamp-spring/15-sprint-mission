package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    //서비스
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"USER_01","유저를 찾을 수 없습니다."),
    DUPLICATE_USER_NAME(HttpStatus.CONFLICT,"USER_02","사용중인 이름입니다."),
    DUPLICATE_USER_EMAIL(HttpStatus.CONFLICT,"USER_03","사용중인 이메일입니다."),

    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND,"CHANNEL_01","채널을 찾을 수 없습니다."),
    PRIVATE_CHANNEL_UPDATE(HttpStatus.UNPROCESSABLE_CONTENT,"CHANNEL_02","비공개 채널은 업데이트할 수 없습니다."),

    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND,"MESSAGE_01","메세지를 찾을 수 없습니다."),

    LOGIN_FAILED_USERNAME(HttpStatus.UNAUTHORIZED,"LOGIN_01","로그인 실패 계정이 없습니다."),
    LOGIN_FAILED_PASSWORD(HttpStatus.UNAUTHORIZED,"LOGIN_02","로그인 실패 비밀번호가 일치하지 않습니다."),

    USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND,"USER_STATUS_01","유저정보를 찾을 수 없습니다."),

    READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND,"READ_STATUS_01","ReadStatus를 찾을 수 없습니다."),
    DUPLICATE_READ_STATUS(HttpStatus.CONFLICT,"READ_STATUS_02","해당 채널, 유저의 ReadStatus가 이미 존재합니다."),

    BINARY_NOT_FOUND(HttpStatus.NOT_FOUND,"BINARY_01","파일정보를 찾을 수 없습니다."),

    ///////////////////////////////////////////////////////////////////////////////
    /// ////////////////////////////////////////////////////////////////////////////
    //스토리지
    STORAGE_INIT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"STORAGE_01","스토리지 초기화를 실패했습니다."),
    FILE_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"STORAGE_02", "파일저장에 실패했습니다."),
    FILE_LOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"STORAGE_03", "파일 불러오기 실패했습니다."),

    //컨트롤러
    //todo 컨트롤러에 파일 io관련 예외 있음
    BINARY_REQUEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"DTO_FROM_01", "파일을 Dto로 전환하는데 실패했습니다."),


    //valid
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "COMMON_01", "입력값이 올바르지 않습니다."),

    //상정되지 않은 오류
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_99", "서버에 문제가 발생했습니다.")
    ;



    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code,  String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
