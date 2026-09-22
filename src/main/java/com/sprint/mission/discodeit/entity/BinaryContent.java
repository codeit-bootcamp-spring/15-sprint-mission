package com.sprint.mission.discodeit.entity;

import lombok.Getter;

@Getter
public class BinaryContent extends Common {

    private static final long serialVersionUID = 1L;

    private final String fileName;
    private final Long fileSize;
    private final String fileType;
    private final byte[] bytes;

    public BinaryContent(String fileName, Long fileSize, String fileType, byte[] bytes) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.bytes = bytes;
    }
}
