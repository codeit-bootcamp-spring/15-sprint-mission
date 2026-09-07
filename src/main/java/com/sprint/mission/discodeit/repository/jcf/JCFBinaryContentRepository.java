package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "jcf", matchIfMissing = true)
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final List<BinaryContent> binaryContents;

    public JCFBinaryContentRepository() {
        this.binaryContents = new ArrayList<>();
    }

    @Override
    public boolean save(BinaryContent binaryContent) {
        return this.binaryContents.add(binaryContent);
    }

    @Override
    public BinaryContent find(UUID id) {
        return this.binaryContents.stream().filter(x-> x.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<BinaryContent> findByIds(List<UUID> ids) {
        List<BinaryContent> results = new ArrayList<>();

        for (UUID id: ids) {
            BinaryContent temp = this.find(id);
            if (temp != null) results.add(temp);
        }

        return results;
    }

    @Override
    public boolean delete(UUID id) {
        return this.binaryContents.removeIf(x-> x.getId().equals(id));
    }
}
