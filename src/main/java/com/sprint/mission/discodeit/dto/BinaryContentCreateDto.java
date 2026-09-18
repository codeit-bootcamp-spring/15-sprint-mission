package com.sprint.mission.discodeit.dto;

public record BinaryContentCreateDto(String fileName,
                                     byte[] bytes,
                                     String contentType) {

}
