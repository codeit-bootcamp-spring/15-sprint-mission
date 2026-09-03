package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository {

    private static final String dataFile = "channel.ser";

    public FileChannelRepository() {
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
    public Channel save(Channel channel) {
        Map<UUID, Channel> data = loadFromFile();
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
    public void delete(UUID channelId) {
        Map<UUID, Channel> data = loadFromFile();
        data.remove(channelId);
        saveToFile(data);
    }
}