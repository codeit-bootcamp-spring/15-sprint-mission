package com.sprint.mission.discodeit.entity;
import lombok.Getter;
@Getter
public final class BinaryContent extends Common {
    private final String fileName;
    private final String contentType;
    private final byte[] bytes;
    public BinaryContent(String fileName, String contentType, byte[] bytes) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.bytes = bytes.clone();
    }
    public byte[] getBytes() {
        return bytes.clone();
    }
}
