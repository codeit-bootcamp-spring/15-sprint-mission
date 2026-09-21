package com.sprint.mission.discodeit.repository.file;

import org.springframework.stereotype.Repository;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.io.*;
import java.util.*;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "file"
)
public class FileMessageRepository implements MessageRepository {

    private final String filePath;
    private Map<UUID, Message> data;

    public FileMessageRepository(
            @Value("${discodeit.repository.file-directory:.discodeit}")
            String fileDirectory
    ) {
        File directory = new File(fileDirectory);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        this.filePath = new File(directory, "messages.ser").getPath();
        this.data = loadData();
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, Message> loadData() {
        File file = new File(filePath);

        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Message>) ois.readObject();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private void saveData() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("Message 파일 저장 실패: " + e.getMessage());
        }
    }

    @Override
    public Message save(Message message) {
        data.put(message.getId(), message);
        saveData();
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
        saveData();
    }
}

