package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;

public class FileChannelService implements ChannelService {

    private final File file = new File("channels.dat");

    private Map<UUID, Channel> channels;

    public FileChannelService() {
        channels = load();
    }

    @Override
    public Channel create(Channel channel) {
        return null;
    }

    @Override
    public Channel create(ChannelType type, String name, String description) {

        Channel channel = new Channel(type, name, description);

        channels.put(channel.getId(), channel);

        save();

        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(channels.get(id));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public Channel update(UUID id, String name, ChannelType type) {
        return null;
    }

    @Override
    public Channel findByid(UUID id) {
        return null;
    }

    @Override
    public void delete(UUID id) {
        channels.remove(id);

        save();
    }

    private void save() {

        try (
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {

            oos.writeObject(channels);

        } catch (IOException e) {
            throw new RuntimeException("Channel 데이터를 저장하는 중 오류가 발생했습니다.", e);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, Channel> load() {

        if (!file.exists()) {
            return new HashMap<>();
        }

        try (
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {

            return (Map<UUID, Channel>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Channel 데이터를 불러오는 중 오류가 발생했습니다.", e);
        }
    }

}
