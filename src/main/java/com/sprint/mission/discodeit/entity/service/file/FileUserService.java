package com.sprint.mission.discodeit.entity.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService, Serializable {

    private final Map<UUID, User> data;

    @Serial
    private static final long serialVersionUID = 1L;

    public FileUserService() {
        this.data = new LinkedHashMap<>();
    }

    @Override
    public User createUser(String name, String phoneNum) {
        User user = new User(name, phoneNum);
        data.put(user.getId(), user); // 저장 로직
        try (FileOutputStream fos = new FileOutputStream("user.ser"); // 저장 로직
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public User getById(UUID id) {
        return data.get(id);
    } // 저장 로직

    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values());
    } // 저장 로직

    @Override
    public User update(UUID id, String name, String phoneNum) {
        User user =data.get(id); // 저장 로직
        user.update(name, phoneNum);
        data.put(id, data.get(id)); // 저장 로직

        try (FileOutputStream fos = new FileOutputStream("user.ser"); // 저장 로직
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public boolean deleteById(UUID id) {
        boolean result = data.remove(id, data.get(id)); // 저장 로직
        // 삭제 여부를 result에 저장
        try (FileOutputStream fos = new FileOutputStream("user.ser"); // 저장 로직
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
