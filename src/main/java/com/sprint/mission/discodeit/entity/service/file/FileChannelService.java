package com.sprint.mission.discodeit.entity.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.service.ChannelService;

import java.io.*;
import java.util.*;


public class FileChannelService implements ChannelService, Serializable {

    private final Map<UUID, Channel> data;
    @Serial
    private static final long serialVersionUID = 1L;

    public FileChannelService() {
        this.data = new LinkedHashMap<>();
    }

    @Override
    public Channel createChannel(String name, String topic) {
        // 새로운 채널을 생성해주고, 그 생성한 채널을 파일에 직렬화하여 넣어주는 로직.
        Channel channel = new Channel(name, topic);
        data.put(channel.getId(), channel); // 저장 로직
        try (FileOutputStream fos = new FileOutputStream("channel.ser"); // 저장 로직
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
    } // 저장 로직

    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(data.values());
    } // 저장 로직

    @Override
    public Channel update(UUID id, String name, String topic) {
        Channel channel = data.get(id);
        channel.update(name, topic); // 조회 // 저장 로직
        data.put(id, channel); // 추가 // 저장 로직

        try (FileOutputStream fos = new FileOutputStream("channel.ser"); // 저장 로직
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return channel;
    }

    @Override
    public boolean deletebyID(UUID id) {
        boolean result = data.remove(id, data.get(id)); // 저장 로직
        // 삭제 여부를 result에 저장
        try (FileOutputStream fos = new FileOutputStream("channel.ser"); // 저장 로직
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
