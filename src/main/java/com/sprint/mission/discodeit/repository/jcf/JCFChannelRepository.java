package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.ArrayList;
import java.util.List;

public class JCFChannelRepository implements ChannelRepository {
    private final List<Channel> data;

    public JCFChannelRepository() {
        this.data = new ArrayList<>();
    }

    @Override
    public boolean create(Channel channel) {
        try {
            this.data.add(channel);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

/*    @Override
    public Channel read(Channel channel) {
        for (Channel c : this.data) {
            if (c.getChannelName().equals(channel.getChannelName())) {
                return c;
            }
        }
        return null;
    }*/

    @Override
    public List<Channel> readAll() {
        return this.data.stream().toList();
    }

    @Override
    public boolean update(Channel channel) {
        for (Channel c : this.data) {
            if (c.getId().equals(channel.getId())) {
                c.setChannelType(channel.getChannelType());
                c.setChannelName(channel.getChannelName());
                c.setChannelDescription(channel.getChannelDescription());
                c.setUpdatedAt(channel.getUpdatedAt());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Channel channel) {
        for (Channel c: this.data) {
            if (c.getId().equals(channel.getId())) {
                this.data.remove(c);
                return true;
            }
        }
        return false;
    }
}
