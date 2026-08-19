package com.sprint.mission.discodeit.entity.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.service.UserService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileUserService implements UserService, Serializable {

    private final Map<UUID, User> data;

    @Serial
    private static final long serialVersionUID = 1L;

    public FileUserService(Map<UUID, User> data) {
        this.data = data;
    }

    @Override
    public User createUser(String name, String phoneNum) {
        User user = new User(name, phoneNum);
        data.put(user.getId(), user);
        try (FileOutputStream fos = new FileOutputStream("user.ser");
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
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User update(UUID id, String name, String phoneNum) {
        data.get(id).update(name, phoneNum);
        data.put(id, data.get(id));

        try (FileOutputStream fos = new FileOutputStream("user.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.get(id);
    }

    @Override
    public boolean deleteById(UUID id) {
        boolean result = data.remove(id, data.get(id));
        // 삭제 여부를 result에 저장
        try (FileOutputStream fos = new FileOutputStream("user.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
