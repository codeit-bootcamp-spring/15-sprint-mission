package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;

public interface ChannelRepository {
    boolean create(Channel channel);
    List<Channel> readAll();
    boolean update(Channel channel);
    boolean delete(Channel channel);
}
