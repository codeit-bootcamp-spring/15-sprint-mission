package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public class FileUserRepository implements UserRepository, Serializable {
    private final Path path;

    public FileUserRepository() {
        path = Paths.get("data", "users");
        try {
            Files.createDirectories(path);
        }
        catch (NoSuchFileException e) {
            System.out.println("폴더 경로가 없음");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean create(User user) {
        Path filePath = Paths.get("data", "users", "user-" + user.getId() + ".ser");
        //중복 검사는 여기서 안한다고 일단 생각하자.

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath.toFile())))
        {
            oos.writeObject(user);
            System.out.println("["+ user.getUsername() + "] 유저 저장 완료");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public User find(UUID userId) {
        Set<User> users = this.findAll();
        for (User user: users) {
            if (user.getId().equals(userId)) return user;
        }

        return null; // Users 가 빈 배열인 경우.
    }

    @Override
    public User findByName(String name) {
        Set<User> users = this.findAll();
        for (User user: users) {
            if (user.getUsername().equals(name)) return user;
        }

        return null;
    }

    @Override
    public Set<User> findAll() {
        File[] files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));
        Set<User> result = new HashSet<>();

        if (files == null || files.length == 0) {
            System.out.println("읽을 파일이 없습니다.");
            return new HashSet<>();
        }

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                result.add((User) obj);

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("파일 역직렬화 실패: " + file.getName());
                return new HashSet<>();
            }
        }

        return result;
    }


    @Override
    public boolean update(User user) {
        Path filePath = Paths.get("data", "users", "user-" + user.getId() + ".ser");

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath.toFile())))
        {
            oos.writeObject(user);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(UUID userId) {
        Path filePath = Paths.get("data", "users", "user-" + userId + ".ser");
        File file = new File(filePath.toUri());
        return file.exists() && file.delete();
    }
}
