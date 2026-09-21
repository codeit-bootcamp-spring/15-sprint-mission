package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final String fileName;
    private final String contentType;
    private final byte[] bytes;
    private final long size;

    private final Instant createdAt;

    public BinaryContent(
            String fileName,
            String contentType,
            byte[] bytes
    ) {
        this.id = UUID.randomUUID();
        this.fileName = fileName;
        this.contentType = contentType;
        this.bytes = bytes;
        this.size = bytes.length;
        this.createdAt = Instant.now();
    }
}