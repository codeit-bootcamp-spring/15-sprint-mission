package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf",
        matchIfMissing = true)
public class JCFBinaryContentRepository implements BinaryContentRepository {

    private final Map<UUID, BinaryContent> binaryContents
            = new ConcurrentHashMap<>();

    // 저장
    @Override
    public void save(BinaryContent binaryContent) {

        binaryContents.put(
                binaryContent.getId(),
                binaryContent
        );
    }

    // id로 하나 조회
    @Override
    public Optional<BinaryContent> findById(UUID id) {

        return Optional.ofNullable(
                binaryContents.get(id)
        );
    }

    // 여러 id로 조회
    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {

        List<BinaryContent> result = new ArrayList<>();

        for (UUID id : ids) {

            BinaryContent binaryContent = binaryContents.get(id);

            if (binaryContent != null) {
                result.add(binaryContent);
            }
        }

        return result;
    }

    // id로 삭제
    @Override
    public void deleteById(UUID id) {

        binaryContents.remove(id);
    }
}