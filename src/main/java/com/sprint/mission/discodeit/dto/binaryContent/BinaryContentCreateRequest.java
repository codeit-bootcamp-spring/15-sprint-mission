package com.sprint.mission.discodeit.dto.binaryContent;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public record BinaryContentCreateRequest(
        String fileName,
        String contentType,
        byte[] bytes
) {

    public static BinaryContentCreateRequest from(MultipartFile binaryContent) {
        try {
            return new BinaryContentCreateRequest(binaryContent.getOriginalFilename(), binaryContent.getContentType(), binaryContent.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
