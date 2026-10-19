package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class BinaryContentCreateRequest {

  @Schema(description = "원본 파일 이름")
  private final String fileName;

  @Schema(description = "파일의 MIME 타입")
  private final String contentType;

  @Schema(description = "파일의 원본 바이트 데이터")
  private final byte[] bytes;

  public BinaryContentCreateRequest(String fileName, String contentType, byte[] bytes) {
    this.fileName = fileName;
    this.contentType = contentType;
    this.bytes = bytes.clone();
  }

  public byte[] getBytes() {
    return bytes.clone();
  }
}
