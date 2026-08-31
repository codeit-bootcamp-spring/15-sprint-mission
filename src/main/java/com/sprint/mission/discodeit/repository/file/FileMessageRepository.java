package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

public class FileMessageRepository implements MessageRepository {


    private final Map<UUID, Message> data;

    public FileMessageRepository() {
        this.data = new LinkedHashMap<>();
    }

    @Override
    public void save(Message message) {
        data.put(message.getId(), message);
        try (FileOutputStream fos = new FileOutputStream("message.ser"); // 저장 로직 (message.ser이라는 실제 파일 아웃픗 만듦)
             ObjectOutputStream oos = new ObjectOutputStream(fos);// message.ser이라는 아웃풋의 주소값을 변수에 보관한다.
        ) {
            oos.writeObject(data); // data 컬렉션 자체를 객체로 직렬화해 message.ser 파일에 기록한다.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Message load(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Message> loadValue() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean delete(UUID id) {
        boolean result = data.remove(id, data.get(id));
        try (FileOutputStream fos = new FileOutputStream("message.ser"); // 저장 로직
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.remove(id, data.get(id));
    }
}
