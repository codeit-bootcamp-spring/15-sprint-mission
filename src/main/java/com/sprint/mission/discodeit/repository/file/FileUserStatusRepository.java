package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.io.*;
import java.util.*;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
@Primary
@Repository
public class FileUserStatusRepository implements UserStatusRepository {

    private final String filePath;
    private Map<UUID, UserStatus> data = new HashMap<>();

    public FileUserStatusRepository(String filePath) {
        this.filePath = filePath;
        load();
    }

    public FileUserStatusRepository() {
        this("user_statuses.ser");
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        data.put(userStatus.getId(), userStatus);
        persist();
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }


    @Override
    public boolean existsByUserId(UUID userId) {
        return data.values().stream()
                .anyMatch(status -> status.getUserId().equals(userId));
    }


    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return data.values().stream()
                .filter(status -> status.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
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
                this.data = (Map<UUID, UserStatus>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}