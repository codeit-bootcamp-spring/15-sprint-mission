package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentRequest.BinaryContentCreateRequest;
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

    // BinaryContent 저장소
    private final BinaryContentRepository binaryContentRepository;


    // 파일 생성
    @Override
    public BinaryContent create(BinaryContentCreateRequest request) {

        BinaryContent binaryContent = new BinaryContent(
                request.contentType(),
                request.fileName(),
                request.fileSize(),
                request.content()
        );

        binaryContentRepository.save(binaryContent);

        return binaryContent;
    }


    // id로 파일 하나 조회
    @Override
    public BinaryContent find(UUID id) {

        return binaryContentRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "BinaryContent를 찾을 수 없습니다. id: " + id
                        )
                );
    }


    // 여러 id로 파일 여러 개 조회
    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {

        return binaryContentRepository.findAllByIdIn(ids);
    }


    // id로 파일 삭제
    @Override
    public void delete(UUID id) {

        binaryContentRepository.deleteById(id);
    }
}