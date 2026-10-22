package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;


@Getter
@MappedSuperclass
public class BaseUpdatableEntity extends BaseEntity{
    @LastModifiedDate
    @Column
    protected Instant  updatedAt;

    protected BaseUpdatableEntity(){

    }

    /*public void setUpdatedAt() {
        this.updatedAt = Instant.now();
    }*/
}
