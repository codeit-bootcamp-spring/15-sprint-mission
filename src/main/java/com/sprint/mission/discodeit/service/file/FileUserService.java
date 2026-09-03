package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileUserService implements UserService {

    private final Path directory;

    public FileUserService() {
        this.directory = Paths.get(System.getProperty("user.dir"), "data", "user");
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User createUser(String nickname) {
        User user = new User(nickname);
        saveToFile(user);
        return user;
    }

    @Override
    public User getUser(UUID id) {
        return findUser(id);
    }

    @Override
    public String getUserNickname(UUID uuid) {
        User user = findUser(uuid);
        return user.getNickName();
    }

    @Override
    public void updateUser(UUID id, String nickname) {
        User user = findUser(id);
        user.updateUser(nickname);
        saveToFile(user);
    }

    @Override
    public List<User> getUserAll() {
        try {
            return Files.list(directory)
                    .filter(p -> p.toString().endsWith(".ser"))
                    .map(this::readFromFile)
                    .toList();
        } catch (IOException e) {
            throw new UncheckedIOException("유저 목록을 불러오지 못했습니다.", e);
        }
    }

    @Override
    public void userDelete(UUID id) {
        Path path = resolvePath(id);
        try {
            if (!Files.deleteIfExists(path)) {
                throw new IllegalArgumentException("존재하지 않는 유저입니다. id = " + id);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("유저 삭제에 실패했습니다. id = " + id, e);
        }
    }

    private Path resolvePath(UUID id) {
        return directory.resolve(id.toString() + ".ser");
    }

    private void saveToFile(User user) {
        Path path = resolvePath(user.getId());
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path))) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new UncheckedIOException("유저 저장에 실패했습니다. id = " + user.getId(), e);
        }
    }

    private User findUser(UUID id) {
        Path path = resolvePath(id);
        if (Files.notExists(path)) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다. id = " + id);
        }
        return readFromFile(path);
    }

    private User readFromFile(Path path) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
            return (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("유저 역직렬화에 실패했습니다. path = " + path, e);
        }
    }
}
