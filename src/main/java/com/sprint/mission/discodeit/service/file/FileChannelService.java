package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;

import java.util.*;

public class FileChannelService implements ChannelService{

    private final ChannelRepository channelRepository = new FileChannelRepository();


    @Override
    public Channel getChannel(UUID id) { return channelRepository.findById(id); }

    @Override
    public List<Channel> getAllChannels() { return channelRepository.findAll(); }

    @Override
    public Channel createChannel(String channelName) {
        Channel channel = new Channel(channelName); // 생성자랑 필드 맞춰서 쓰기
        return channelRepository.save(channel);
    }

    @Override
    public Channel updateChannel(UUID id, String channelName) { return channelRepository.update(id, channelName); }

    @Override
    public Channel deleteChannel(UUID id) { return channelRepository.delete(id); }
}
