package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
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
public class FileChannelRepository implements ChannelRepository {

    private final String dataFile;
    private final ReadStatusRepository readStatusRepository;

    public FileChannelRepository(@Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory,
                                 ReadStatusRepository readStatusRepository) {
        this.dataFile = fileDirectory + "/channel.ser";
        this.readStatusRepository = readStatusRepository;

        File file = new File(dataFile);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        if (!file.exists()) {
            saveToFile(new HashMap<>());
        }
    }

    private void saveToFile(Map<UUID, Channel> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, Channel> loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized Channel save(Channel channel) {
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
    public List<Channel> readAllByUserId(UUID userId) {
        Map<UUID, Channel> data = loadFromFile();
        return data.values().stream()
                .filter(channel -> {
                    if (channel.getChannelType() == ChannelType.PUBLIC) {
                        return true;
                    }
                    return readStatusRepository.readAllByChannelId(channel.getId()).stream()
                            .anyMatch(readStatus -> readStatus.getUserId().equals(userId));
                })
                .toList();
    }

    @Override
    public synchronized void delete(UUID channelId) {
        Map<UUID, Channel> data = loadFromFile();
        data.remove(channelId);
        saveToFile(data);
    }
}