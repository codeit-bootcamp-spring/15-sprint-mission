package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileReadStatusRepository implements ReadStatusRepository {

    private final String dataFile;

    public FileReadStatusRepository(@Value("${discodeit.repository.file-directory:.discodeit") String fileDirectory) {
        this.dataFile = fileDirectory + "/read_status.ser";

        File file = new File(dataFile);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        if (!file.exists()) {
            saveToFile(new HashMap<>());
        }
    }

    private void saveToFile(Map<UUID, ReadStatus> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, ReadStatus> loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            return (Map<UUID, ReadStatus>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized ReadStatus save(ReadStatus readStatus) {
        Map<UUID, ReadStatus> data = loadFromFile();
        data.put(readStatus.getId(), readStatus);
        saveToFile(data);
        return readStatus;
    }

    @Override
    public ReadStatus read(UUID id) {
        Map<UUID, ReadStatus> data = loadFromFile();
        return data.get(id);
    }

    @Override
    public List<ReadStatus> readAllByUserId(UUID userId) {
        Map<UUID, ReadStatus> data = loadFromFile();
        return data.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId)).toList();
    }

    @Override
    public List<ReadStatus> readAllByChannelId(UUID channelId) {
        Map<UUID, ReadStatus> data = loadFromFile();
        return data.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId)).toList();
    }

    @Override
    public synchronized void delete(UUID id) {
        Map<UUID, ReadStatus> data = loadFromFile();
        data.remove(id);
        saveToFile(data);
    }

    @Override
    public synchronized void deleteAllByChannelId(UUID channelId) {
        Map<UUID, ReadStatus> data = loadFromFile();
        data.values().removeIf(readStatus -> readStatus.getChannelId().equals(channelId));
        saveToFile(data);
    }

    @Override
    public boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
        Map<UUID, ReadStatus> data = loadFromFile();
        return data.values().stream()
                .anyMatch(readStatus -> readStatus.getUserId().equals(userId)
                        && readStatus.getChannelId().equals(channelId));
    }
}