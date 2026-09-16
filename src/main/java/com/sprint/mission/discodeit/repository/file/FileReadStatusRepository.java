package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "file"
)
public class FileReadStatusRepository implements ReadStatusRepository {

    private final Path filePath;

    public FileReadStatusRepository(
            @Value("${discodeit.repository.file-directory:.discodeit}")
            String fileDirectory
    ) {
        this.filePath = Path.of(
                fileDirectory,
                "readStatuses.ser"
        );
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {

        Map<UUID, ReadStatus> readStatuses = load();

        readStatuses.put(
                readStatus.getId(),
                readStatus
        );

        saveAll(readStatuses);

        return readStatus;
    }

    @Override
    public List<ReadStatus> findAll() {

        Map<UUID, ReadStatus> readStatuses = load();

        return new ArrayList<>(
                readStatuses.values()
        );
    }

    @Override
    public void deleteByChannelId(UUID channelId) {

        Map<UUID, ReadStatus> readStatuses = load();

        readStatuses.values().removeIf(
                readStatus -> readStatus.getChannelId().equals(channelId)
        );

        saveAll(readStatuses);
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {

        Map<UUID, ReadStatus> readStatuses = load();

        List<ReadStatus> result = new ArrayList<>();

        for (ReadStatus readStatus : readStatuses.values()) {
            if (readStatus.getChannelId().equals(channelId)) {
                result.add(readStatus);
            }
        }

        return result;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {

        Map<UUID, ReadStatus> readStatuses = load();

        return Optional.ofNullable(
                readStatuses.get(id)
        );
    }

    @Override
    public Optional<ReadStatus> findByChannelIdAndUserId(
            UUID channelId,
            UUID userId
    ) {

        Map<UUID, ReadStatus> readStatuses = load();

        for (ReadStatus readStatus : readStatuses.values()) {
            if (
                    readStatus.getChannelId().equals(channelId)
                            && readStatus.getUserId().equals(userId)
            ) {
                return Optional.of(readStatus);
            }
        }

        return Optional.empty();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {

        Map<UUID, ReadStatus> readStatuses = load();

        List<ReadStatus> result = new ArrayList<>();

        for (ReadStatus readStatus : readStatuses.values()) {
            if (readStatus.getUserId().equals(userId)) {
                result.add(readStatus);
            }
        }

        return result;
    }

    @Override
    public void deleteById(UUID id) {

        Map<UUID, ReadStatus> readStatuses = load();

        readStatuses.remove(id);

        saveAll(readStatuses);
    }

    // Map 전체를 파일에 저장
    private void saveAll(Map<UUID, ReadStatus> readStatuses) {

        try {

            Files.createDirectories(filePath.getParent());

            try (
                    ObjectOutputStream outputStream =
                            new ObjectOutputStream(
                                    new FileOutputStream(filePath.toFile())
                            )
            ) {
                outputStream.writeObject(readStatuses);
            }

        } catch (IOException e) {

            throw new RuntimeException(
                    "ReadStatus 저장 중 오류가 발생했습니다.",
                    e
            );
        }
    }

    // 파일에서 Map 전체 읽기
    @SuppressWarnings("unchecked")
    private Map<UUID, ReadStatus> load() {

        File file = filePath.toFile();

        if (!file.exists()) {
            return new HashMap<>();
        }

        try (
                ObjectInputStream inputStream =
                        new ObjectInputStream(
                                new FileInputStream(file)
                        )
        ) {

            return (Map<UUID, ReadStatus>)
                    inputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {

            throw new RuntimeException(
                    "ReadStatus 조회 중 오류가 발생했습니다.",
                    e
            );
        }
    }
}