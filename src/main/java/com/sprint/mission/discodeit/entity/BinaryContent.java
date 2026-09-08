package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;


@Getter
public class BinaryContent implements Serializable {
    private final UUID id;
    private final Instant createdAt;
    private final String fileName;
    private final  byte[] file;


    public BinaryContent(String fileName, byte[] file){
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.fileName=fileName;
        this.file = file;

    }


}
