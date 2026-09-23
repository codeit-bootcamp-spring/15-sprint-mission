package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "file"
)
public class FileMessageRepository implements MessageRepository {

    private final Path filePath;

    public FileMessageRepository(
            @Value("${discodeit.repository.file-directory:.discodeit}")
            String fileDirectory
    ) {
        this.filePath = Path.of(
                fileDirectory,
                "messages.ser"
        );
    }

    private void saveAll(List<Message> messages) {

        try {

            Files.createDirectories(filePath.getParent());

            try (
                    FileOutputStream fos = new FileOutputStream(filePath.toFile());
                    ObjectOutputStream oos = new ObjectOutputStream(fos)
            ) {
                oos.writeObject(messages);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private List<Message> loadAll() {

        File file = filePath.toFile();

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (
                FileInputStream fis =
                        new FileInputStream(file);

                ObjectInputStream ois =
                        new ObjectInputStream(fis)
        ) {

            return (List<Message>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Message save(Message message) {

        List<Message> messages =
                new ArrayList<>(loadAll());

        boolean found = false;

        for (int i = 0; i < messages.size(); i++) {

            if (messages.get(i).getId().equals(message.getId())) {
                messages.set(i, message);
                found = true;
                break;
            }
        }

        if (!found) {
            messages.add(message);
        }

        saveAll(messages);

        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {

        for (Message message : loadAll()) {

            if (message.getId().equals(id)) {
                return Optional.of(message);
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Message> findAll() {
        return loadAll();
    }

    @Override
    public boolean existsById(UUID id) {

        for (Message message : loadAll()) {

            if (message.getId().equals(id)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void deleteById(UUID id) {

        List<Message> messages =
                new ArrayList<>(loadAll());

        messages.removeIf(
                message -> message.getId().equals(id)
        );

        saveAll(messages);
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {

        return loadAll().stream()
                .filter(message ->
                        message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void deleteByChannelId(UUID channelId) {

        List<Message> messages =
                new ArrayList<>(loadAll());

        messages.removeIf(message ->
                message.getChannelId().equals(channelId));

        saveAll(messages);
    }
}