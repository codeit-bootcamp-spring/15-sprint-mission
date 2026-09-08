package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
abstract public class BaseClass implements Serializable {
    private final UUID id;
    private final Instant  createdAt;
    protected Instant  updatedAt;

    public BaseClass(){
        this.id = UUID.randomUUID();
        Instant now = Instant.now();
        this.createdAt=now;
        this.updatedAt=now;
    }
    public BaseClass(UUID id, Instant createdAt, Instant updatedAt){
        this.id=id;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;
    }

    public void setUpdatedAt() {
        this.updatedAt = Instant.now();
    }



    //public abstract String ToString();
}
