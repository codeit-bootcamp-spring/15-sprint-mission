package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;

public class BinaryContentDto {


    private UUID id;
    private Instant createdAt;
    private String fileName;
    private String contentType;
    private long size;

    public BinaryContentDto(
            UUID id,
            Instant createdAt,
            String fileName,
            String contentType,
            long size
    ) {
        this.id = id;
        this.createdAt = createdAt;
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
    }

    public UUID getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public long getSize() {
        return size;
    }
}
