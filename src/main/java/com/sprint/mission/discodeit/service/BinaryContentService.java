package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Response.BinaryContentDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContentDto find(UUID id);
    List<BinaryContentDto> findAllByIds(List<UUID> binaryContentIds);
    ResponseEntity<?> download(UUID id);

}
