package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUserRepository implements UserRepository {

    private static final String FILE_PATH = "users.ser";


    private void saveAll(List<User> users) {

        try (
                FileOutputStream fos = new FileOutputStream(FILE_PATH);
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {

            oos.writeObject(users);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @SuppressWarnings("unchecked")
    private List<User> loadAll() {

        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (
                FileInputStream fis = new FileInputStream(FILE_PATH);
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {

            return (List<User>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public User save(User user) {

        List<User> users = new ArrayList<>(loadAll());

        boolean found = false;

        for (int i = 0; i < users.size(); i++) {

            if (users.get(i).getId().equals(user.getId())) {
                users.set(i, user);
                found = true;
                break;
            }
        }

        if (!found) {
            users.add(user);
        }

        saveAll(users);

        return user;
    }


    @Override
    public User findById(UUID id) {

        for (User user : loadAll()) {

            if (user.getId().equals(id)) {
                return user;
            }
        }

        return null;
    }


    @Override
    public List<User> findAll() {

        return loadAll();
    }


    @Override
    public void deleteById(UUID id) {

        List<User> users = new ArrayList<>(loadAll());

        users.removeIf(user -> user.getId().equals(id));

        saveAll(users);
    }
}