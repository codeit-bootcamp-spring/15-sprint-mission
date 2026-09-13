package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFBinaryContentRepository implements BinaryContentRepository {

    private final Map<UUID, BinaryContent> data;

    public JCFBinaryContentRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        data.put(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    @Override
    public BinaryContent read(UUID id) {
        return data.get(id);
    }

    @Override
    public List<BinaryContent> readAllByIdIn(List<UUID> ids) {
        return data.values().stream()
                .filter(bc -> ids.contains(bc.getId()))
                .toList();
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}