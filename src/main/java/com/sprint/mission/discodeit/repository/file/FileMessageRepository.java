package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository {

    private static final String FILE_PATH = "messages.ser";


    private void saveAll(List<Message> messages) {

        try (
                FileOutputStream fos = new FileOutputStream(FILE_PATH);
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {

            oos.writeObject(messages);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @SuppressWarnings("unchecked")
    private List<Message> loadAll() {

        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (
                FileInputStream fis = new FileInputStream(FILE_PATH);
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {

            return (List<Message>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Message save(Message message) {

        List<Message> messages = new ArrayList<>(loadAll());

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
    public Message findById(UUID id) {

        for (Message message : loadAll()) {

            if (message.getId().equals(id)) {
                return message;
            }
        }

        return null;
    }


    @Override
    public List<Message> findAll() {

        return loadAll();
    }


    @Override
    public void deleteById(UUID id) {

        List<Message> messages = new ArrayList<>(loadAll());

        messages.removeIf(message -> message.getId().equals(id));

        saveAll(messages);
    }
}