package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContent create(BinaryContentCreateRequest request) {
        BinaryContent binaryContent = new BinaryContent(
                request.fileName(),
                request.fileSize(),
                request.fileType(),
                request.bytes()
        );
        return binaryContentRepository.save(binaryContent);
    }

    @Override
    public BinaryContent read(UUID id) {
        return binaryContentRepository.read(id);
    }

    @Override
    public List<BinaryContent> readAllByIdIn(List<UUID> ids) {
        return binaryContentRepository.readAllByIdIn(ids);
    }

    @Override
    public void delete(UUID id) {
        binaryContentRepository.delete(id);
    }
}