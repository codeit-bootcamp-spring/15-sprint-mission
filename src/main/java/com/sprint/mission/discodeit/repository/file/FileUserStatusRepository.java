package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Repository
public class FileUserStatusRepository implements UserStatusRepository {
    private final Path path;

    public FileUserStatusRepository() {
        path = Paths.get("data", "userStatus");
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean save(UserStatus userStatus) {
        Path filePath = Paths.get("data", "userStatus", "userStatus-" + userStatus.getId() + ".ser");

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath.toFile())))
        {
            oos.writeObject(userStatus);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public UserStatus find(UUID userid) {
        File[] files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));

        if (files == null || files.length == 0) {
            System.out.println("읽을 파일이 없습니다.");
            return null;
        }

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                UserStatus temp = (UserStatus) obj;
                if (temp.getUserId().equals(userid)) {
                    return temp;
                }

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("파일 역직렬화 실패: " + file.getName());
            }
        }

        return null;
    }

    @Override
    public boolean delete(UUID userId) {
        Path filePath = Paths.get("data", "userStatus", "userStatus-" + userId + ".ser");
        File file = new File(filePath.toUri());
        return file.exists() && file.delete();
    }
}
