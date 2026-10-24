package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.base.*;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaries;
  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentStorage binaryContentStorage;

  public BinaryContentDto create(BinaryContentCreateRequest request) {
    byte[] bytes = request.getBytes();
    BinaryContent meta = binaries.save(
        new BinaryContent(request.getFileName(), request.getContentType(), (long) bytes.length));
    binaryContentStorage.put(meta.getId(), bytes);
    return binaryContentMapper.toDto(meta);
  }

  @Transactional(readOnly = true)
  public BinaryContentDto find(UUID id) {
    return binaryContentMapper.toDto(binaries.findById(id)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 BinaryContent: " + id)));
  }

  @Transactional(readOnly = true)
  public List<BinaryContentDto> findAllByIdIn(List<UUID> ids) {
    return binaries.findAllByIdIn(ids).stream().map(binaryContentMapper::toDto).toList();
  }

  public void delete(UUID id) {
    binaries.deleteById(id);
  }
}
