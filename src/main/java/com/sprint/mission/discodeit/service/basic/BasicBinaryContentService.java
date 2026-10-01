package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
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
    public BinaryContentDto create(BinaryContentCreateRequest request) {

        BinaryContent binaryContent = new BinaryContent(
                request.fileName(),
                request.contentType(),
                request.bytes()
        );

        BinaryContent savedBinaryContent =
                binaryContentRepository.save(binaryContent);

        return new BinaryContentDto(
                savedBinaryContent.getId(),
                savedBinaryContent.getFileName(),
                savedBinaryContent.getContentType(),
                savedBinaryContent.getBytes(),
                savedBinaryContent.getSize(),
                savedBinaryContent.getCreatedAt()
        );
    }

    @Override
    public BinaryContentDto find(UUID id) {

        BinaryContent binaryContent = binaryContentRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 바이너리 콘텐츠입니다. ID: " + id
                        )
                );

        return new BinaryContentDto(
                binaryContent.getId(),
                binaryContent.getFileName(),
                binaryContent.getContentType(),
                binaryContent.getBytes(),
                binaryContent.getSize(),
                binaryContent.getCreatedAt()
        );
    }

    @Override
    public List<BinaryContentDto> findAllByIdIn(List<UUID> ids) {

        return binaryContentRepository.findAllByIdIn(ids)
                .stream()
                .map(binaryContent -> new BinaryContentDto(
                        binaryContent.getId(),
                        binaryContent.getFileName(),
                        binaryContent.getContentType(),
                        binaryContent.getBytes(),
                        binaryContent.getSize(),
                        binaryContent.getCreatedAt()
                ))
                .toList();
    }

    @Override
    public void delete(UUID id) {

        binaryContentRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 바이너리 콘텐츠입니다. ID: " + id
                        )
                );

        binaryContentRepository.deleteById(id);
    }

}
