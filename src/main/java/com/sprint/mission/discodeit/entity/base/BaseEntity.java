package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;
import org.springframework.data.annotation.CreatedDate;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) // 생성/수정 시간 자동 감지 리스너
public abstract class BaseEntity {

  @Id
  @Column(columnDefinition = "uuid", updatable = false, nullable = false)
  private UUID id;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @LastModifiedDate
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  // JPA 스펙 및 프록시 생성을 위한 기본 생성자 (상속용 protected)
  protected BaseEntity() {
    this.id = UUID.randomUUID();
  }

  // 외부에서 특정 ID를 지정하여 생성할 수 있도록 열어두는 생성자
  protected BaseEntity(UUID id) {
    this.id = (id != null) ? id : UUID.randomUUID();
  }
}

