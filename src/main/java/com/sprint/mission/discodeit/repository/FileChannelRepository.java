package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
public class FileChannelRepository {

    private final File file = new File("channels.dat");

    private Map<UUID, Channel> channels;

    public FileChannelRepository() {
        channels = load();
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, Channel> load() {
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(file))) {

            return (Map<UUID, Channel>) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(
                    "Channel 데이터를 불러오는 중 오류가 발생했습니다.", e
            );
        }
    }

    private void save() {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(file))) {

            out.writeObject(channels);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Channel 데이터를 저장하는 중 오류가 발생했습니다.", e
            );
        }
    }

    public Channel save(Channel channel) {
        channels.put(channel.getId(), channel);
        save();
        return channel;
    }

    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(channels.get(id));
    }

    public List<Channel> findAll() {
        return new ArrayList<>(channels.values());
    }

    public void delete(UUID id) {
        channels.remove(id);
        save();
    }

}
