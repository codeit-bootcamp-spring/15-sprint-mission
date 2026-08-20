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
        try (FileOutputStream fos = new FileOutputStream("message.ser"); // 저장 로직
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(data);
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
