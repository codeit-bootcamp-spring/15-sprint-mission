package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.io.*;
import java.util.*;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
@Primary
@Repository
public class FileReadStatusRepository implements ReadStatusRepository {

    private final String filePath;
    private Map<UUID, ReadStatus> data = new HashMap<>();

    public FileReadStatusRepository(String filePath) {
        this.filePath = filePath;
        load();
    }

    public FileReadStatusRepository() {
        this("read_statuses.ser");
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        data.put(readStatus.getId(), readStatus);
        persist();
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return data.values().stream()
                .filter(s -> s.getUserId().equals(userId) && s.getChannelId().equals(channelId))
                .findFirst();
    }

    @Override
    public boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
        return data.values().stream()
                .anyMatch(s -> s.getUserId().equals(userId) && s.getChannelId().equals(channelId));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return data.values().stream()
                .filter(s -> s.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(s -> s.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
        persist();
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        data.values().removeIf(s -> s.getChannelId().equals(channelId));
        persist();
    }

    private void persist() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void load() {
        File file = new File(filePath);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                this.data = (Map<UUID, ReadStatus>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}