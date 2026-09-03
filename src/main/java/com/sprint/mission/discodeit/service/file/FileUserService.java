package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {
    private final String filePath = "users.ser";
    private Map<UUID, User> data;

    public FileUserService() {
        this.data = loadData();
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, User> loadData() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, User>) ois.readObject();
        } catch (Exception e) {
            System.err.println("User 파일 읽기 실패 : " + e.getMessage());
            return new HashMap<>();
        }
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("User 파일 저장 실패 : " + e.getMessage());
        }
    }

    // 💡 UserService 인터페이스에 password가 없는 기본 규격일 경우 (기존 유지)
    // 만약 password가 들어간 인터페이스라면 (String username, String email, String password)로 수정해 주세요.
    @Override
    public User create(String username, String email) {
        User user = new User(username, email);
        data.put(user.getId(), user);
        saveData();
        return user;
    }

    @Override
    public User find(UUID id) {
        User user = data.get(id);
        if (user == null) {
            throw new NoSuchElementException("존재하지 않는 사용자입니다. ID: " + id);
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User update(UUID id, String username, String email) {
        User user = find(id); // 없으면 NoSuchElementException 발생
        user.update(username, email);
        saveData();
        return user;
    }

    @Override
    public void delete(UUID id) {
        find(id); // 존재 여부 검증
        data.remove(id);
        saveData();
    }
}
