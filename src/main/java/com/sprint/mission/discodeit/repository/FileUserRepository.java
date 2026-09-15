package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.io.*;
import java.util.*;

public class FileUserRepository {

    private File file = new File("users.dat");

    private Map<UUID, User> users;

    public FileUserRepository() {
        users = load();
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, User> load() {
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {

            return (Map<UUID, User>) in.readObject();
        }catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("User 데이터를 불러오는 중 오류를 발생했습니다", e );
        }

    }
    // 파일에 Map 전체 저장
    private void save() {
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {

            out.writeObject(users);

        }catch (IOException e) {
            throw new RuntimeException("User 데이터를 저장하는 중 오류가 발생했습니다.", e);
        }
    }
    //User를 Map에 넣고 파일 저장
    public User save (User user) {
        users.put(user.getId(), user);
        save();
        return user;
    }
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(users.get(id));
    }
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
    public void delete(UUID id) {
        users.remove(id);
        save();
    }
}


