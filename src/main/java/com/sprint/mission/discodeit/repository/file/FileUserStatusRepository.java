package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "file"
)
public class FileUserStatusRepository implements UserStatusRepository {

    private final Path filePath;

    public FileUserStatusRepository(
            @Value("${discodeit.repository.file-directory:.discodeit}")
            String fileDirectory
    ) {
        this.filePath = Path.of(
                fileDirectory,
                "userStatuses.ser"
        );
    }

    @Override
    public void save(UserStatus userStatus) {

        Map<UUID, UserStatus> userStatuses = load();

        userStatuses.put(
                userStatus.getUserId(),
                userStatus
        );

        saveAll(userStatuses);
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {

        return load().values()
                .stream()
                .filter(userStatus ->
                        userStatus.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {

        return Optional.ofNullable(
                load().get(userId)
        );
    }

    @Override
    public List<UserStatus> findAll() {

        return new ArrayList<>(
                load().values()
        );
    }

    @Override
    public void deleteById(UUID id) {

        Map<UUID, UserStatus> userStatuses = load();

        userStatuses.entrySet()
                .removeIf(entry ->
                        entry.getValue().getId().equals(id));

        saveAll(userStatuses);
    }

    @Override
    public void deleteByUserId(UUID userId) {

        Map<UUID, UserStatus> userStatuses = load();

        userStatuses.remove(userId);

        saveAll(userStatuses);
    }

    private void saveAll(
            Map<UUID, UserStatus> userStatuses
    ) {

        try {

            // 저장할 폴더가 없으면 생성
            Files.createDirectories(filePath.getParent());

            try (
                    ObjectOutputStream outputStream =
                            new ObjectOutputStream(
                                    new FileOutputStream(filePath.toFile())
                            )
            ) {

                outputStream.writeObject(userStatuses);
            }

        } catch (IOException e) {
            throw new RuntimeException(
                    "UserStatus 저장 중 오류가 발생했습니다.",
                    e
            );
        }
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, UserStatus> load() {

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

            return (Map<UUID, UserStatus>)
                    inputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(
                    "UserStatus 조회 중 오류가 발생했습니다.",
                    e
            );
        }
    }
}