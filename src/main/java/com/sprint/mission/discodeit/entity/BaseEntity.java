package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;


@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
abstract public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,updatable = false)
    @CreatedDate
    private Instant  createdAt;

    protected BaseEntity(){

    }
}
