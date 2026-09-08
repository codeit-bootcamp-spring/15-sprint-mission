package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService {

    private final File file = new File("messages.dat");

    private Map<UUID, Message> messages;

    public FileMessageService() {
        messages = load();
    }

        @Override
    public Message create(Message message) {
        messages.put(message.getId(), message);
        save();
        return message;
    }

    @Override
    public Message create(String content, UUID channelId, UUID autorId) {
        return null;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(messages.get(id));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messages.values());
    }

    @Override
    public Optional<Message> update(UUID id, String content) {

        Message message = messages.get(id);

        if (message == null) {
            return null;
        }

        message.setContent(content);

        messages.put(id, message);

        save();

        return Optional.of(message);
    }

    @Override
    public void delete(UUID id) {

        messages.remove(id);

        save();
    }

    @Override
    public void like(UUID messageId, UUID userId) {

        Message message = messages.get(messageId);

        if (message == null) {
            return;
        }

        message.getLikeUserIds().add(userId);

        save();
    }

    @Override
    public void unlike(UUID messageId, UUID userId) {

        Message message = messages.get(messageId);

        if (message == null) {
            return;
        }

        message.getLikeUserIds().remove(userId);

        save();
    }

    @Override
    public Set<UUID> getLikeUserIds(UUID messageId) {
        return Set.of();
    }

    @Override
    public Set<UUID> getlikeUserIds(UUID messageId) {

        Message message = messages.get(messageId);

        if (message == null) {
            return new HashSet<>();
        }

        return message.getLikeUserIds();
    }

    private void save() {

        try (
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {

            oos.writeObject(messages);

        } catch (IOException e) {
            throw new RuntimeException("Message 데이터를 저장하는 중 오류가 발생했습니다.", e);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, Message> load() {

        if (!file.exists()) {
            return new HashMap<>();
        }

        try (
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {

            return (Map<UUID, Message>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Message 데이터를 불러오는 중 오류가 발생했습니다.", e);
        }
    }
}