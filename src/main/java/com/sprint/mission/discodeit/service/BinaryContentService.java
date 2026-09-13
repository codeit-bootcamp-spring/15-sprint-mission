package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.*;
import java.util.*;
public interface BinaryContentService {
    BinaryContent create(BinaryContentCreateRequest request);
    BinaryContent find(UUID id);
    List<BinaryContent> findAllByIdIn(List<UUID> ids);
    void delete(UUID id);
}
