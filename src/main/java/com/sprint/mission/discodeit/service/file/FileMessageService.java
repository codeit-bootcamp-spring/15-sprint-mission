package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileMessageService implements MessageService {

    private static final String dataFile = "message.ser";

    public FileMessageService() {
        File file = new File(dataFile);
        if (!file.exists()) {
            saveToFile(new HashMap<>());
        }
    }

    // 객체 직렬화
    private void saveToFile(Map<UUID, Message> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 객체 역직렬화
    @SuppressWarnings("unchecked")
    private Map<UUID, Message> loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            return (Map<UUID, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Message create(String contents, UUID userId, UUID channelId) {
        Map<UUID, Message> data = loadFromFile();
        Message message = new Message(contents, userId, channelId);
        data.put(message.getId(), message);
        saveToFile(data);
        return message;
    }

    @Override
    public Message read(UUID messageId) {
        Map<UUID, Message> data = loadFromFile();
        return data.get(messageId);
    }

    @Override
    public List<Message> readAll() {
        Map<UUID, Message> data = loadFromFile();
        return data.values().stream().toList();
    }

    @Override
    public Message update(UUID messageId, String contents) {
        Map<UUID, Message> data = loadFromFile();
        Message message = data.get(messageId);
        message.update(contents);
        saveToFile(data);
        return message;
    }

    @Override
    public void delete(UUID messageId) {
        Map<UUID, Message> data = loadFromFile();
        data.remove(messageId);
        saveToFile(data);
    }
}