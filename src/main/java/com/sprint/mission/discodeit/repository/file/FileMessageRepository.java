package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "file")
public class FileMessageRepository implements MessageRepository, Serializable {
    private final Path path;

    public FileMessageRepository(@Value("${discodeit.repository.file-directory}") String directory) {
        path = Paths.get(directory, "messages");
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
    public Message find(UUID id) {
        Path filePath = path.resolve("message-" + id + ".ser");
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            return (Message) ois.readObject();
        } catch (NoSuchFileException e) {
            throw new IllegalArgumentException("존재하지 않는 메시지입니다.", e);
        }
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        File[] files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));
        List<Message> result = new ArrayList<>();

        if (files == null || files.length == 0) {
            System.out.println("읽을 파일이 없습니다.");
            return new ArrayList<>();
        }

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                Message message = (Message) obj;
                if (message.getChannelId().equals(channelId)) result.add(message);

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("파일 역직렬화 실패: " + file.getName());
                return new ArrayList<>();
            }
        }

        return result;
    }

    @Override
    public List<Message> findByAuthorId(UUID authorId) {
        File[] files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));
        List<Message> result = new ArrayList<>();

        if (files == null || files.length == 0) {
            System.out.println("읽을 파일이 없습니다.");
            return new ArrayList<>();
        }

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                Message message = (Message) obj;
                if (message.getAuthorId().equals(authorId)) result.add(message);

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("파일 역직렬화 실패: " + file.getName());
                return new ArrayList<>();
            }
        }

        return result;
    }



    @Override
    public List<Message> findAll() {
        File[] files = path.toFile().listFiles((dir, name) -> name.endsWith(".ser"));
        List<Message> result = new ArrayList<>();

        if (files == null || files.length == 0) {
            System.out.println("읽을 파일이 없습니다.");
            return new ArrayList<>();
        }

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                result.add((Message) obj);

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("파일 역직렬화 실패: " + file.getName());
                return result;
            }
        }

        return result;
    }

    @Override
    public boolean create(Message message) {
        Path filePath = path.resolve("message-" + message.getId() + ".ser");

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile())))
        {
            oos.writeObject(message);
            System.out.println("메세지 저장 완료");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Message update(Message message) {
        Path filePath = path.resolve("message-" + message.getId() + ".ser");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile())))
        {
            oos.writeObject(message);
            return message;
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("메시지 정상 저장 실패");
    }

    @Override
    public void delete(UUID id) {
        Path filePath = path.resolve("message-" + id + ".ser");

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.out.println("삭제 실패");
        }
    }
}
