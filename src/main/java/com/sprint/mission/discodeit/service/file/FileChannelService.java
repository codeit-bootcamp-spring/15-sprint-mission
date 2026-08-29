package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileChannelService implements ChannelService {

    private static final String dataFile = "channel.ser";

    public FileChannelService() {
        File file = new File(dataFile);
        if (!file.exists()) {
            saveToFile(new HashMap<>());
        }
    }

    // 객체 직렬화
    private void saveToFile(Map<UUID, Channel> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 객체 역직렬화
    @SuppressWarnings("unchecked")
    private Map<UUID, Channel> loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Channel create(String channelName) {
        Map<UUID, Channel> data = loadFromFile();
        Channel channel = new Channel(channelName);
        data.put(channel.getId(), channel);
        saveToFile(data);
        return channel;
    }

    @Override
    public Channel read(UUID channelId) {
        Map<UUID, Channel> data = loadFromFile();
        return data.get(channelId);
    }

    @Override
    public List<Channel> readAll() {
        Map<UUID, Channel> data = loadFromFile();
        return data.values().stream().toList();
    }

    @Override
    public Channel update(UUID channelId, String channelName) {
        Map<UUID, Channel> data = loadFromFile();
        Channel channel = data.get(channelId);
        channel.update(channelName);
        saveToFile(data);
        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        Map<UUID, Channel> data = loadFromFile();
        data.remove(channelId);
        saveToFile(data);
    }
}