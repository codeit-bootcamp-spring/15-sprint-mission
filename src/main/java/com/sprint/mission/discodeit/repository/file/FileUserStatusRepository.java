package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
//import lombok.Value;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(
        name = "discodeit.repository.type",
        havingValue = "file"
)
public class FileUserStatusRepository implements UserStatusRepository {

    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileUserStatusRepository(
            @Value("${discodeit.repository.file-directory:data}")
            String fileDirectory
    ) {
        this.DIRECTORY = Paths.get(
                System.getProperty("user.dir"),
                fileDirectory,
                UserStatus.class.getSimpleName()
        );

        try {
            Files.createDirectories(DIRECTORY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private Path resolvePath(UUID id) {
        return DIRECTORY.resolve(id + EXTENSION);
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        Path path = resolvePath(userStatus.getId());

        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(userStatus);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        Path path = resolvePath(id);

        if (Files.notExists(path)) {
            return Optional.empty();
        }

        try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            return Optional.of((UserStatus) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserStatus> findAll() {
        try {
            return Files.list(DIRECTORY)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try (
                                FileInputStream fis =
                                        new FileInputStream(path.toFile());
                                ObjectInputStream ois =
                                        new ObjectInputStream(fis)
                        ) {
                            return (UserStatus) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {

        return findAll().stream().filter(u -> u.getUserId().equals(userId)).findFirst();
    }

    @Override
    public void deleteById(UUID id) {
        Path path = resolvePath(id);

        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByUserId(UUID userId) {
        UserStatus userStatus = findAll().stream().filter(u -> u.getUserId().equals(userId))
                .findFirst().orElseThrow(() -> new NoSuchElementException("userId를 가진 유저스테이터스 없음 : " + userId));
        deleteById(userStatus.getId());
    }

    @Override
    public boolean existsById(UUID id) {
        return Files.exists(resolvePath(id));
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return findAll().stream()
                .anyMatch(u -> u.getUserId().equals(userId));
    }
}