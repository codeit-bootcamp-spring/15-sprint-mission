package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  USER_NOT_FOUND("USER_NOT_FOUND", "사용자를 찾을 수 없습니다."),
  USER_ALREADY_EXISTS("USER_ALREADY_EXISTS", "이미 존재하는 사용자입니다."),
  CHANNEL_NOT_FOUND("CHANNEL_NOT_FOUND", "채널을 찾을 수 없습니다."),
  MESSAGE_NOT_FOUND("MESSAGE_NOT_FOUND", "메시지를 찾을 수 없습니다."),
  BINARY_CONTENT_NOT_FOUND("BINARY_CONTENT_NOT_FOUND", "파일을 찾을 수 없습니다."),
  CHANNEL_UPDATE_NOT_ALLOWED("CHANNEL_UPDATE_NOT_ALLOWED", "비공개 채널은 수정할 수 없습니다.");

  private final String code;
  private final String message;
}