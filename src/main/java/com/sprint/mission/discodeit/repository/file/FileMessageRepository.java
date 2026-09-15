package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
public class FileMessageRepository {

    private final File file = new File("messages.dat");

    private Map<UUID, Message> messages;

    public FileMessageRepository() {
        messages = load();
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, Message> load() {
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(file))) {

            return (Map<UUID, Message>) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(
                    "Message 데이터를 불러오는 중 오류가 발생했습니다.", e
            );
        }
    }

    private void save() {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(file))) {

            out.writeObject(messages);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Message 데이터를 저장하는 중 오류가 발생했습니다.", e
            );
        }
    }

    public Message save(Message message) {
        messages.put(message.getId(), message);
        save();
        return message;
    }

    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(messages.get(id));
    }

    public List<Message> findAll() {
        return new ArrayList<>(messages.values());
    }

    public void delete(UUID id) {
        messages.remove(id);
        save();
    }
}
