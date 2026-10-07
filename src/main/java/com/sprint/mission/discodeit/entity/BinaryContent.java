package com.sprint.mission.discodeit.entity;


import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@Table(name = "binary_contents")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BinaryContent extends BaseEntity {

  @Column(name = "file_name", nullable = false)
  private String fileName;

  @Column(name = "size", nullable = false)
  private Long size;

  @Column(name = "content_type", length = 100, nullable = false)
  private String contentType;

  // 신규 생성 시 (ID 자동 발급)
  public BinaryContent(String fileName, Long size, String contentType) {
    super();
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
  }

  // 특정 ID를 지정해야 하는 경우
  public BinaryContent(UUID id, String fileName, Long size, String contentType) {
    super(id);
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
  }
}