package com.sprint.mission.discodeit.entity.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.service.ChannelService;

import java.util.*;


public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data;

    public JCFChannelService() {
        this.data = new LinkedHashMap<>();
    }

    @Override
    public Channel createChannel(String name, String topic) {
        Channel channel = new Channel(name, topic);
        data.put(channel.getId(), channel);
        return channel;
    }

    public Map<UUID, Channel> getData() {
        return data;
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
        // 수정 : 조회 + 추가
        data.get(id).update(name, topic);
        data.put(id, data.get(id));
        return data.get(id);
    }

    @Override
    public boolean deletebyID(UUID id) {
        return data.remove(id, data.get(id));
    }


    // data 필드를 활용해 생성, 조회, 수정, 삭제하는 메소드를 구현하세요.

}
