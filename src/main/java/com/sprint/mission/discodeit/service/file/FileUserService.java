package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileUserService implements UserService {

    private static final String dataFile = "user.ser";

    public FileUserService() {
        File file = new File(dataFile);
        if (!file.exists()) {
            saveToFile(new HashMap<>());
        }
    }

    // 객체 직렬화
    private void saveToFile(Map<UUID, User> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 객체 역직렬화
    @SuppressWarnings("unchecked")
    private Map<UUID, User> loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            return (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User create(String userName, String email, String password) {
        Map<UUID, User> data = loadFromFile();
        User user = new User(userName, email, password);
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
    public User update(UUID userId, String userName, String email, String password) {
        Map<UUID, User> data = loadFromFile();
        User user = data.get(userId);
        user.update(userName, email, password);
        saveToFile(data);
        return user;
    }

    @Override
    public void delete(UUID userId) {
        Map<UUID, User> data = loadFromFile();
        data.remove(userId);
        saveToFile(data);
    }
}