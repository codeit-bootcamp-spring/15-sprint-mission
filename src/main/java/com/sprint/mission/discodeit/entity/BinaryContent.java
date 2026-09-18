package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter

public class BinaryContent implements Serializable {

    private final UUID id;
    private final String fileName;
    private final Long size;
    private final String contentType;
    private final byte[] bytes;
    private final Instant createdAt;

    public BinaryContent(String fileName, Long size, String contentType, byte[] bytes) {
        this.id = UUID.randomUUID();
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.bytes = bytes;
        this.createdAt = Instant.now();
    }


}
