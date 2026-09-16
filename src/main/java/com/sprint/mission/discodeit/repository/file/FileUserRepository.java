package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
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
public class FileUserRepository implements UserRepository {

    private final String dataFile;

    public FileUserRepository(@Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory) {
        this.dataFile = fileDirectory + "/user.ser";

        File file = new File(dataFile);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        if (!file.exists()) {
            saveToFile(new HashMap<>());
        }
    }

    private void saveToFile(Map<UUID, User> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, User> loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            return (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized User save(User user) {
        Map<UUID, User> data = loadFromFile();
        data.put(user.getId(), user);
        saveToFile(data);
        return user;
    }

    @Override
    public User read(UUID userId) {
        Map<UUID, User> data = loadFromFile();
        return data.get(userId);
    }

    @Override
    public List<User> readAll() {
        Map<UUID, User> data = loadFromFile();
        return data.values().stream().toList();
    }

    @Override
    public synchronized void delete(UUID userId) {
        Map<UUID, User> data = loadFromFile();
        data.remove(userId);
        saveToFile(data);
    }

    @Override
    public boolean existsByUserName(String userName) {
        Map<UUID, User> data = loadFromFile();
        return data.values().stream().anyMatch(user -> user.getUserName().equals(userName));
    }

    @Override
    public boolean existsByEmail(String email) {
        Map<UUID, User> data = loadFromFile();
        return data.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }
}