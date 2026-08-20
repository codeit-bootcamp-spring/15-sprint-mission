package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

public class FileUserRepository implements UserRepository {

    private final Map<UUID,User> data;

    public FileUserRepository() {
        this.data = new LinkedHashMap<>();
    }

    @Override
    public void save(User user) {
        data.put(user.getId(), user); // 저장 로직
        try (FileOutputStream fos = new FileOutputStream("user.ser"); // 저장 로직
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User load(UUID id) {
        return data.get(id);
    }

    @Override
    public List<User> loadValue() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean delete(UUID id) {
        boolean result = data.remove(id, data.get(id));
        try (FileOutputStream fos = new FileOutputStream("user.ser"); // 저장 로직
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.remove(id, data.get(id));
    }
}
