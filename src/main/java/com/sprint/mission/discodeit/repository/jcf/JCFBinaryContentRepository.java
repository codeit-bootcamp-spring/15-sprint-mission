package com.sprint.mission.discodeit.repository.jcf;
import org.springframework.stereotype.Repository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.util.*;
@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> data = new HashMap<>();
    @Override
    public BinaryContent save(BinaryContent entity) {
        data.put(entity.getId(), entity);
        return entity;
    }
    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }
    @Override
    public List<BinaryContent> findAll() {
        return List.copyOf(data.values());
    }
    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }
    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        List<BinaryContent> result = new ArrayList<>();
        for (UUID id : ids) {
            Optional<BinaryContent> content = findById(id);
            if (content.isPresent() && !result.contains(content.get())) {
                result.add(content.get());
            }
        }
        return result;
    }
}
