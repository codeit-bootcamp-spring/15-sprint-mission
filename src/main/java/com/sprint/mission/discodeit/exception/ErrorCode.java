package com.sprint.mission.discodeit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  // User
  USER_NOT_FOUND("USER_001", "회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  DUPLICATE_EMAIL("USER_002", "이미 사용 중인 이메일입니다.", HttpStatus.CONFLICT),
  DUPLICATE_USERNAME("USER_003", "이미 사용 중인 사용자 이름입니다.", HttpStatus.CONFLICT),
  USER_PROFILE_NOT_FOUND("USER_004", "프로필 이미지가 없습니다.", HttpStatus.NOT_FOUND),

  // Auth
  INVALID_CREDENTIALS("AUTH_001", "로그인 정보가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),

  // Channel
  CHANNEL_NOT_FOUND("CHANNEL_001", "채널을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  NOT_CHANNEL_PARTICIPANT("CHANNEL_002", "해당 채널의 참여자가 아닙니다.", HttpStatus.FORBIDDEN),
  PRIVATE_CHANNEL_PARTICIPANT_ADD_NOT_ALLOWED("CHANNEL_003", "PRIVATE 채널은 생성 후 참여자를 추가할 수 없습니다.",
      HttpStatus.BAD_REQUEST),
  PUBLIC_CHANNEL_REQUIRED("CHANNEL_004", "PUBLIC 채널만 조회할 수 있습니다.", HttpStatus.BAD_REQUEST),
  PRIVATE_CHANNEL_PARTICIPANT_UPDATE_NOT_ALLOWED("CHANNEL_005", "PRIVATE 채널의 참여자는 변경할 수 없습니다.",
      HttpStatus.BAD_REQUEST),
  PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED("CHANNEL_006", "PRIVATE 채널은 수정할 수 없습니다.",
      HttpStatus.BAD_REQUEST),

  // BinaryContent
  BINARY_CONTENT_NOT_FOUND("BINARY_001", "컨텐츠를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  INVALID_IMAGE_CONTENT_TYPE("BINARY_002", "이미지 파일만 업로드 가능합니다.", HttpStatus.BAD_REQUEST),
  EMPTY_FILE("BINARY_003", "빈 파일은 업로드할 수 없습니다.", HttpStatus.BAD_REQUEST),
  MISSING_FILE_NAME("BINARY_004", "파일 이름이 없습니다.", HttpStatus.BAD_REQUEST),

  // Message
  MESSAGE_NOT_FOUND("MES_001", "메세지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

  // ReadStatus
  READ_STATUS_NOT_FOUND("READ_001", "존재하지 않는 상태입니다.", HttpStatus.NOT_FOUND),
  READ_STATUS_ALREADY_EXISTS("READ_002", "이미 존재하는 읽음 상태입니다.", HttpStatus.CONFLICT),

  // UserStatus
  USER_STATUS_NOT_FOUND("USER_STATUS_001", "사용자의 접속 상태를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  USER_STATUS_ALREADY_EXISTS("USER_STATUS_002", "이미 존재하는 접속 상태입니다.", HttpStatus.CONFLICT);

  private final String code;

  private final String message;

  private final HttpStatus httpStatus;

}
