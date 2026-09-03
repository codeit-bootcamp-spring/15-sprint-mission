package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class FileUserRepository implements UserRepository {

    private final Path directory;

    public FileUserRepository() {
        this.directory = Paths.get(System.getProperty("user.dir"), "data", "user");
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new UncheckedIOException("유저 데이터 디렉토리를 생성할 수 없습니다.", e);
        }
    }

    private Path resolvePath(UUID id) {
        return directory.resolve(id.toString() + ".ser");
    }

    @Override
    public User createUser(User user) {
        Path path = resolvePath(user.getId());
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path))) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new UncheckedIOException("유저 저장에 실패했습니다. id = " + user.getId(), e);
        }
        return user;
    }

    @Override
    public Optional<User> getUser(UUID id) {
        Path path = resolvePath(id);
        if (Files.notExists(path)) {
            return Optional.empty();
        }
        return Optional.of(readUser(path));
    }

    @Override
    public List<User> getUserAll() {
        try (Stream<Path> paths = Files.list(directory)) {
            return paths
                    .filter(path -> path.toString().endsWith(".ser"))
                    .map(this::readUser)
                    .toList();
        } catch (IOException e) {
            throw new UncheckedIOException("유저 목록을 불러오지 못했습니다.", e);
        }
    }

    @Override
    public void deleteUser(UUID id) {
        try {
            Files.deleteIfExists(resolvePath(id));
        } catch (IOException e) {
            throw new UncheckedIOException("유저 삭제에 실패했습니다. id = " + id, e);
        }
    }

    @Override
    public boolean existsById(UUID id) {
        return Files.exists(resolvePath(id));
    }

    private User readUser(Path path) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
            return (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("유저 역직렬화에 실패했습니다. path = " + path, e);
        }
    }
}
