package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
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
public class FileBinaryContentRepository implements BinaryContentRepository {
    private final Path path;

    public FileBinaryContentRepository(@Value("${discodeit.repository.file-directory}") String directory) {
        path = Paths.get(directory, "binaryContents");
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean save(BinaryContent binaryContent) {
        Path filePath = path.resolve("binaryContent-" + binaryContent.getId() + ".ser");

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath.toFile())))
        {
            oos.writeObject(binaryContent);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public BinaryContent find(UUID id) {
        File[] files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));

        if (files == null || files.length == 0) {
            System.out.println("읽을 파일이 없습니다.");
            return null;
        }

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                BinaryContent temp = (BinaryContent) obj;
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
    public List<BinaryContent> findByIds(List<UUID> ids) {
        File[] files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));
        List<BinaryContent> result = new ArrayList<>();

        if (files == null || files.length == 0) {
            System.out.println("읽을 파일이 없습니다.");
            return new ArrayList<>();
        }

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                BinaryContent temp = (BinaryContent) obj;
                if (ids.contains(temp.getId())) {
                    result.add(temp);
                }

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("파일 역직렬화 실패: " + file.getName());
            }
        }

        return result;
    }

    @Override
    public boolean delete(UUID id) {
        Path filePath = path.resolve("binaryContent-" + id + ".ser");
        File file = new File(filePath.toUri());
        return file.exists() && file.delete();
    }
}
