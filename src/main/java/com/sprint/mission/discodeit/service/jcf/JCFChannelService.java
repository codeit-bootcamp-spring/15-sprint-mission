package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;

public class JCFChannelService implements ChannelService {
    private final List<Channel> data;

    public JCFChannelService() {
        this.data = new ArrayList<>();
    }

    @Override
    public void create(Channel channel) {
        this.data.add(channel);
    }

    @Override
    public Channel read(Channel channel) {
        for (Channel u : this.data) {
            if (u.getId().equals(channel.getId())) return u;
        }
        return null;
    }

    @Override
    public List<Channel> readAll() {
        return this.data;
    }

    @Override
    public void update(Channel user, String data) {
        for (Channel c : this.data) {
            if (c.getId().equals(user.getId())) {
                c.setChannel(data);
                c.setUpdatedAt();
            }
        }
        System.out.println("변경 완료");
    }

    @Override
    public void delete(Channel user) {
        this.data.remove(user);
    }

}
