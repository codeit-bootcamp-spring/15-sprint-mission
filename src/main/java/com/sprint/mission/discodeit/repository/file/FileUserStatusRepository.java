package com.sprint.mission.discodeit.repository.file;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
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
public class FileUserStatusRepository implements UserStatusRepository {

    private final String dataFile;

    public FileUserStatusRepository(@Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory) {
        this.dataFile = fileDirectory + "/user_status.ser";

        File file = new File(dataFile);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        if (!file.exists()) {
            saveToFile(new HashMap<>());
        }
    }

    private void saveToFile(Map<UUID, UserStatus> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, UserStatus> loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            return (Map<UUID, UserStatus>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized UserStatus save(UserStatus userStatus) {
        Map<UUID, UserStatus> data = loadFromFile();
        data.put(userStatus.getId(), userStatus);
        saveToFile(data);
        return userStatus;
    }

    @Override
    public UserStatus read(UUID id) {
        Map<UUID, UserStatus> data = loadFromFile();
        return data.get(id);
    }

    @Override
    public List<UserStatus> readAll() {
        Map<UUID, UserStatus> data = loadFromFile();
        return data.values().stream().toList();
    }

    @Override
    public UserStatus readByUserId(UUID userId) {
        Map<UUID, UserStatus> data = loadFromFile();
        return data.values().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public synchronized void delete(UUID id) {
        Map<UUID, UserStatus> data = loadFromFile();
        data.remove(id);
        saveToFile(data);
    }

    @Override
    public synchronized void deleteByUserId(UUID userId) {
        Map<UUID, UserStatus> data = loadFromFile();
        data.values().removeIf(userStatus -> userStatus.getUserId().equals(userId));
        saveToFile(data);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        Map<UUID, UserStatus> data = loadFromFile();
        return data.values().stream()
                .anyMatch(userStatus -> userStatus.getUserId().equals(userId));
    }
}