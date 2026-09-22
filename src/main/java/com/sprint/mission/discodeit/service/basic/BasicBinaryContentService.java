package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.exception.NotFoundException;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;

  @Override
  public BinaryContent create(BinaryContentCreateRequest bccr) {
    BinaryContent binaryContent = new BinaryContent(bccr.fileName(), bccr.contentType(),
        bccr.bytes());

    binaryContentRepository.save(binaryContent);
    return binaryContent;
  }

  @Override
  public BinaryContent find(UUID id) {
    return binaryContentRepository.find(id);
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
    return binaryContentRepository.findByIds(ids);
  }

  @Override
  public void delete(UUID id) {
    BinaryContent binaryContent = binaryContentRepository.find(id);
    if (binaryContent == null) {
      throw new NotFoundException("찾을 수 없음");
    }

    binaryContentRepository.delete(id);
  }
}
