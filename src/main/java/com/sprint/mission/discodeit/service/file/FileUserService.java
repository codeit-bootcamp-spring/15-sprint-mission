package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;


public class FileUserService implements UserService {

    private final File file = new File("users.dat");
    private Map<UUID, User> users;

    public FileUserService() {
        users = load();

    }

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User create(String username, String email, String password) throws IOException {
        User user = new User(username, email, password);
        users.put(user.getId(), user);
        save();
        return user;

    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public Optional<Object> update(UUID id, String name) {
        return Optional.empty();
    }

    @Override
    public List<User> findall() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void delete(UUID id) throws IOException {
        users.remove(id);
        save();
    }

    private void save() throws IOException {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(users);

        } catch (IOException e) {
            e.printStackTrace();


        }
    }
    private Map<UUID ,User> load() {

        if (!file.exists()) return new HashMap<>();

        try(ObjectInputStream in =
                new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, User>) in.readObject();
        }catch (Exception e) {
            return new HashMap<>();
        }

    }
}


