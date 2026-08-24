package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository {

    private static final String FILE_PATH = "channels.ser";


    private void saveAll(List<Channel> channels) {

        try (
                FileOutputStream fos = new FileOutputStream(FILE_PATH);
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {

            oos.writeObject(channels);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @SuppressWarnings("unchecked")
    private List<Channel> loadAll() {

        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (
                FileInputStream fis = new FileInputStream(FILE_PATH);
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {

            return (List<Channel>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Channel save(Channel channel) {

        List<Channel> channels = new ArrayList<>(loadAll());

        boolean found = false;

        for (int i = 0; i < channels.size(); i++) {

            if (channels.get(i).getId().equals(channel.getId())) {
                channels.set(i, channel);
                found = true;
                break;
            }
        }

        if (!found) {
            channels.add(channel);
        }

        saveAll(channels);

        return channel;
    }


    @Override
    public Channel findById(UUID id) {

        for (Channel channel : loadAll()) {

            if (channel.getId().equals(id)) {
                return channel;
            }
        }

        return null;
    }


    @Override
    public List<Channel> findAll() {

        return loadAll();
    }


    @Override
    public void deleteById(UUID id) {

        List<Channel> channels = new ArrayList<>(loadAll());

        channels.removeIf(channel -> channel.getId().equals(id));

        saveAll(channels);
    }
}