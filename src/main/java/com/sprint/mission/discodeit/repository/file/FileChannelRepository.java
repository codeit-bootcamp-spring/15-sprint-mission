package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> data;

    public FileChannelRepository() {
        this.data = new LinkedHashMap<>();
    }

    @Override
    public void save(Channel channel) {
        data.put(channel.getId(), channel);
        try (FileOutputStream fos = new FileOutputStream("channel.ser"); // 저장 로직
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Channel load(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Channel> loadValue() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean delete(UUID id) {
        boolean result = data.remove(id, data.get(id));
        try (FileOutputStream fos = new FileOutputStream("channel.ser"); // 저장 로직
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.remove(id, data.get(id));
    }
}
