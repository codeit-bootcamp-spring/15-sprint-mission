package com.sprint.mission.discodeit.entity.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.service.ChannelService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;



public class FileChannelService implements ChannelService, Serializable {

    private final Map<UUID, Channel> data;
    @Serial
    private static final long serialVersionUID = 1L;

    public FileChannelService(Map<UUID, Channel> data) {
        this.data = data;
    }

    @Override
    public Channel createChannel(String name, String topic) {
        // 새로운 채널을 생성해주고, 그 생성한 채널을 파일에 직렬화하여 넣어주는 로직.
        Channel channel = new Channel(name, topic);
        data.put(channel.getId(), channel);
        try (FileOutputStream fos = new FileOutputStream("channel.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return channel;
    }

    @Override
    public Channel getById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel update(UUID id, String name, String topic) {
        data.get(id).update(name, topic); // 조회
        data.put(id, data.get(id)); // 추가

        try (FileOutputStream fos = new FileOutputStream("channel.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.get(id);
    }

    @Override
    public boolean deletebyID(UUID id) {
        boolean result = data.remove(id, data.get(id));
        // 삭제 여부를 result에 저장
        try (FileOutputStream fos = new FileOutputStream("channel.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
