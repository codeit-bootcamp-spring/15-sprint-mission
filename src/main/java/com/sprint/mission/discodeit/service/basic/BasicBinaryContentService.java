package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.io.IOException;
import java.io.InputStream;


@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;

    @Override
    public BinaryContentDto create(BinaryContentCreateRequest request) {

        BinaryContent binaryContent = new BinaryContent(
            request.fileName(),
            request.contentType(),
            request.bytes().length
        );

        BinaryContent savedBinaryContent =
                binaryContentRepository.save(binaryContent);

        binaryContentStorage.put(savedBinaryContent.getId(), request.bytes());

        return new BinaryContentDto(
            savedBinaryContent.getId(),
            savedBinaryContent.getFileName(),
            savedBinaryContent.getContentType(),
            readBytes(savedBinaryContent.getId()),
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
            readBytes(binaryContent.getId()),
            binaryContent.getSize(),
            binaryContent.getCreatedAt()
        );
    }

    @Override
    public List<BinaryContentDto> findAllByIdIn(List<UUID> ids) {

        return binaryContentRepository.findAllById(ids)
                .stream()
            .map(binaryContent -> new BinaryContentDto(
                binaryContent.getId(),
                binaryContent.getFileName(),
                binaryContent.getContentType(),
                readBytes(binaryContent.getId()),
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

        if (TransactionSynchronizationManager.isActualTransactionActive()
            && TransactionSynchronizationManager.isSynchronizationActive()) {

            TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        binaryContentStorage.delete(id);
                    }
                }
            );

        } else {
            binaryContentStorage.delete(id);
        }
    }

    private byte[] readBytes(UUID id) {
        try (InputStream inputStream = binaryContentStorage.get(id)) {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            throw new java.io.UncheckedIOException(
                "파일 읽기 실패: " + id, e
            );
        }
    }
}
