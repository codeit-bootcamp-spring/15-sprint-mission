package com.sprint.mission.discodeit.dto;
import lombok.Getter;
@Getter
public class BinaryContentCreateRequest {
    private final String fileName;
    private final String contentType;
    private final byte[] bytes;
    public BinaryContentCreateRequest(String fileName, String contentType, byte[] bytes) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.bytes = bytes.clone();
    }
    public byte[] getBytes() {
        return bytes.clone();
    }
}
