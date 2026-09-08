package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "file")
public class FileReadStatusRepository implements ReadStatusRepository {
    private final Path path;

    public FileReadStatusRepository(@Value("${discodeit.repository.file-directory}") String directory) {
        path = Paths.get(directory, "readStatus");
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean save(ReadStatus readStatus) {
        Path filePath = path.resolve("readStatus-" + readStatus.getId() + ".ser");

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath.toFile())))
        {
            oos.writeObject(readStatus);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ReadStatus isAlreadyExist(UUID userId, UUID channelId) {
        File[] files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));

        if (files == null || files.length == 0) {
            System.out.println("읽을 파일이 없습니다.");
            return null;
        }

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                ReadStatus temp = (ReadStatus) obj;
                if (temp.getUserId().equals(userId) && temp.getChannelId().equals(channelId)) {
                    return temp;
                }

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("파일 역직렬화 실패: " + file.getName());
            }
        }

        return null;
    }

    @Override
    public ReadStatus find(UUID id) {
        File[] files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));

        if (files == null || files.length == 0) {
            System.out.println("읽을 파일이 없습니다.");
            return null;
        }

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                ReadStatus temp = (ReadStatus) obj;
                if (temp.getId().equals(id)) {
                    return temp;
                }

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("파일 역직렬화 실패: " + file.getName());
            }
        }

        return null;
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        File[] files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));
        List<ReadStatus> result = new ArrayList<>();

        if (files == null || files.length == 0) {
            return null;
        }

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                ReadStatus temp = (ReadStatus) obj;
                if (temp.getUserId().equals(userId)) {
                    result.add(temp);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("파일 역직렬화 실패: " + file.getName());
            }
        }

        return result;
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        File[] files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));
        List<ReadStatus> result = new ArrayList<>();

        if (files == null || files.length == 0) {
            System.out.println("읽을 파일이 없습니다.");
            return new ArrayList<>();
        }

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                ReadStatus temp = (ReadStatus) obj;
                if (temp.getChannelId().equals(channelId)) {
                    result.add(temp);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("파일 역직렬화 실패: " + file.getName());
                return new ArrayList<>();
            }
        }

        return result;
    }

    @Override
    public boolean delete(UUID id) {
        Path filePath = path.resolve("readStatus-" + id + ".ser");
        File file = new File(filePath.toUri());
        return file.exists() && file.delete();
    }
}
