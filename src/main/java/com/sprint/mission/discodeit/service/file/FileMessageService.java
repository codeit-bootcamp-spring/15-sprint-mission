package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService {
    private final String filePath = "messages.ser";
    private Map<UUID, Message> data;

    private final UserService userService;
    private final ChannelService channelService;

    public FileMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
        this.data = loadData();
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, Message> loadData() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Message>) ois.readObject();
        } catch (Exception e) {
            System.err.println("Message 파일 읽기 실패: " + e.getMessage());
            return new HashMap<>();
        }
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("Message 파일 저장 실패: " + e.getMessage());
        }
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        // channelService.find()와 userService.find()가 내부에서 존재 여부 검증 및 예외 처리를 수행함
        channelService.find(channelId);
        userService.find(authorId);

        Message message = new Message(content, channelId, authorId);
        data.put(message.getId(), message);
        saveData();
        return message;
    }

    @Override
    public Message find(UUID id) {
        Message message = data.get(id);
        if (message == null) {
            throw new NoSuchElementException("존재하지 않는 메시지입니다. ID: " + id);
        }
        return message;
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Message update(UUID id, String content) {
        Message message = find(id); // 없으면 NoSuchElementException 발생
        message.update(content);
        saveData();
        return message;
    }

    @Override
    public void delete(UUID id) {
        find(id); // 존재 여부 검증
        data.remove(id);
        saveData();
    }
}
