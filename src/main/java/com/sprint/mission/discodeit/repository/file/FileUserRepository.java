package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "file"
)
public class FileUserRepository implements UserRepository {

    private final Path filePath;

    public FileUserRepository(
            @Value("${discodeit.repository.file-directory:.discodeit}")
            String fileDirectory
    ) {
        this.filePath = Path.of(
                fileDirectory,
                "users.ser"
        );
    }


    private void saveAll(List<User> users) {

        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {

            oos.writeObject(users);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private List<User> loadAll() {

        File file = filePath.toFile();

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (
                FileInputStream fis = new FileInputStream(filePath.toFile());
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
    public Optional<User> findById(UUID id) {

        for (User user : loadAll()) {
            if (user.getId().equals(id)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return loadAll();
    }

    @Override
    public boolean existsById(UUID id) {

        for (User user : loadAll()) {
            if (user.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteById(UUID id) {

        List<User> users = new ArrayList<>(loadAll());
        users.removeIf(user -> user.getId().equals(id));
        saveAll(users);
    }

    @Override
    public Optional<User> findByName(String username) {
        return findAll().stream()
                .filter(user -> user.getName().equals(username))
                .findFirst();
    }
}