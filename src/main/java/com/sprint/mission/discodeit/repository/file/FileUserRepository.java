package com.sprint.mission.discodeit.repository.file;

import org.springframework.stereotype.Repository;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
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
public class FileUserRepository implements UserRepository {

    private final String filePath;
    private Map<UUID, User> data;

    public FileUserRepository(
            @Value("${discodeit.repository.file-directory:.discodeit}")
            String fileDirectory
    ) {
        File directory = new File(fileDirectory);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        this.filePath = new File(directory, "users.ser").getPath();
        this.data = loadData();
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, User> loadData() {
        File file = new File(filePath);

        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, User>) ois.readObject();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private void saveData() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("User 파일 저장 실패: " + e.getMessage());
        }
    }

    @Override
    public User save(User user) {
        data.put(user.getId(), user);
        saveData();
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return data.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return data.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
        saveData();
    }
}


