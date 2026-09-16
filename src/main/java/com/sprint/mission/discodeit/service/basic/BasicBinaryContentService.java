package com.sprint.mission.discodeit.service.basic;
import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.*;
import java.util.*;
@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaries;

    public BinaryContent create(BinaryContentCreateRequest request) {
        return binaries.save(new BinaryContent(request.getFileName(), request.getContentType(), request.getBytes()));
    }

    public BinaryContent find(UUID id) {
        return binaries.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 BinaryContent: " + id));
    }

    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return binaries.findAllByIdIn(ids);
    }

    public void delete(UUID id) {

        binaries.deleteById(id);
    }
}
