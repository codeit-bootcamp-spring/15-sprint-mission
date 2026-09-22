package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class BinaryContent implements Serializable {

  private final UUID id;
  private final Instant createdAt;
  private final String fileName;  // 파일의 이름
  private final String contentType; // 미디어 타입, jpg, png...
  private final byte[] bytes; // 용량? 뭐 어짜피 계산하면 되긴하는데 일단 넣어놓음.


  public long getSize() {
    return bytes.length;
  }

  public BinaryContent(String fileName, String contentType, byte[] bytes) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.fileName = fileName;
    this.contentType = contentType;
    this.bytes = bytes;
  }
}
