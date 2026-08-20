package com.sprint.mission.discodeit.entity.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.service.ChannelService;

import java.util.*;


public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data; // 저장 로직

    public JCFChannelService() {
        this.data = new LinkedHashMap<>();
    }  // 저장 로직

    @Override
    public Channel createChannel(String name, String topic) {
        Channel channel = new Channel(name, topic);
        data.put(channel.getId(), channel); // 저장 로직
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
        // 수정 : 조회 + 추가
        Channel channel = data.get(id); // 저장 로직
        channel.update(name, topic); // 저장 로직
        data.put(id, data.get(id));
        return channel;
    }

    @Override
    public boolean deletebyID(UUID id) {
        return data.remove(id, data.get(id)); // 저장 로직
    }


    // data 필드를 활용해 생성, 조회, 수정, 삭제하는 메소드를 구현하세요.

}
