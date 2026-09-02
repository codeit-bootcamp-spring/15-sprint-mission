package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FileMessageRepository implements MessageRepository, Serializable {
    private final Path path;

    public FileMessageRepository() {
        path = Paths.get("data", "messages");
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
    public List<Message> readAll() {
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
                return new ArrayList<>();
            }
        }

        return result;
    }

    @Override
    public boolean create(Message message) {
        //중복 검사는 여기서 안한다고 일단 생각하자.
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("data\\messages\\message-" + message.getId() + ".ser")
        )) {
            oos.writeObject(message);
            System.out.println("["+ message.getMessage() + "...] 메세지 저장 완료"); //10자 이상,이하 무조건 테스트
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Message message) {
        try (
                ObjectOutputStream oos = new ObjectOutputStream(
                        new FileOutputStream("data\\messages\\message-" + message.getId() + ".ser")
                ))
        {
            oos.writeObject(message);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Message message) {
        File file = new File("data\\messages\\message-" + message.getId() + ".ser");
        return file.exists() && file.delete();
    }
}
