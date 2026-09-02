package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentSevice {
    BinaryContent create(String fileName, String contentType, byte[] bytes);
    BinaryContent read(UUID id);
    List<BinaryContent> readAll(List<UUID> id);
    void delete(UUID id);
}
