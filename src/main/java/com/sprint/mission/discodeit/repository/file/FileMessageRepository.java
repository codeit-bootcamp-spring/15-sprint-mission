package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileMessageRepository implements MessageRepository {

    private final String dataFile;

    public FileMessageRepository(@Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory) {
        this.dataFile = fileDirectory + "/message.ser";

        File file = new File(dataFile);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        if (!file.exists()) {
            saveToFile(new HashMap<>());
        }
    }

    private void saveToFile(Map<UUID, Message> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, Message> loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            return (Map<UUID, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized Message save(Message message) {
        Map<UUID, Message> data = loadFromFile();
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
    public List<Message> readAllByChannelId(UUID channelId) {
        Map<UUID, Message> data = loadFromFile();
        return data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public synchronized void delete(UUID messageId) {
        Map<UUID, Message> data = loadFromFile();
        data.remove(messageId);
        saveToFile(data);
    }
}