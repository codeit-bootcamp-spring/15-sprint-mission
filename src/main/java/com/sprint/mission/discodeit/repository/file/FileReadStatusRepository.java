package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.io.*;
import java.util.*;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "file"
)
public class FileReadStatusRepository implements ReadStatusRepository {

    private final String filePath;
    private Map<UUID, ReadStatus> data;

    public FileReadStatusRepository(
            @Value("${discodeit.repository.file-directory:.discodeit}")
            String fileDirectory
    ) {
        File directory = new File(fileDirectory);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        this.filePath = new File(directory, "readStatuses.ser").getPath();
        this.data = loadData();
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, ReadStatus> loadData() {
        File file = new File(filePath);

        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, ReadStatus>) ois.readObject();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private void saveData() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("ReadStatus 파일 저장 실패: " + e.getMessage());
        }
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        data.put(readStatus.getId(), readStatus);
        saveData();
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(
            UUID userId,
            UUID channelId
    ) {
        return data.values().stream()
                .filter(readStatus ->
                        readStatus.getUserId().equals(userId)
                                && readStatus.getChannelId().equals(channelId)
                )
                .findFirst();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return data.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
        saveData();
    }
}
