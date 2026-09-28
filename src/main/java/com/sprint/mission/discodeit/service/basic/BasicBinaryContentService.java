package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;

  @Override
  public BinaryContentResponse create(BinaryContentCreateRequest request) {
    BinaryContent binaryContent = new BinaryContent(
        request.fileName(),
        request.fileSize(),
        request.fileType(),
        request.bytes()
    );
    return toResponse(binaryContentRepository.save(binaryContent));
  }

  @Override
  public BinaryContentResponse read(UUID id) {
    BinaryContent binaryContent = binaryContentRepository.read(id);
    if (binaryContent == null) {
      throw new NoSuchElementException("존재하지 않는 바이너리 파일");
    }
    return toResponse(binaryContent);
  }

  @Override
  public List<BinaryContentResponse> readAllByIdIn(List<UUID> ids) {
    return binaryContentRepository.readAllByIdIn(ids).stream()
        .map(this::toResponse)
        .toList();
  }

  @Override
  public void delete(UUID id) {
    binaryContentRepository.delete(id);
  }

  private BinaryContentResponse toResponse(BinaryContent binaryContent) {
    return new BinaryContentResponse(
        binaryContent.getId(),
        binaryContent.getCreatedAt(),
        binaryContent.getFileName(),
        binaryContent.getFileSize(),
        binaryContent.getFileType(),
        binaryContent.getBytes()
    );
  }
}