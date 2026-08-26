package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(ChannelType channelType, String channelName, String channelDescription) {
        Channel channel1 = new Channel(channelType, channelName, channelDescription);
        if (channelRepository.create(channel1)) {
            return channel1;
        }
        return null;
    }

    @Override
    public Channel read(String channelName) {
        List<Channel> channels = this.readAll();
        if (channels.isEmpty()) {
            System.out.println("현재 저장된 채널이 없습니다.");
            return null;
        }

        for (Channel channel: channels) {
            if (channel.getChannelName().equals(channelName)) return channel;
        }

        return null;
    }

    @Override
    public List<Channel> readAll() {
        return channelRepository.readAll();
    }

    @Override
    public void update(Channel channel, ChannelType channelType, String channelName, String channelDescription) {
        if (channel == null) {
            System.out.println("저장소에서 해당 채널을 찾을 수 없습니다.");
            return;
        }

        channel.setChannelType(channelType);
        channel.setChannelName(channelName);
        channel.setChannelDescription(channelDescription);
        channel.autoSetUpdatedAt();

        if (channelRepository.update(channel)) {
            System.out.println("정상적으로 메세지 업데이트가 되었습니다.");
        }
        else {
            System.out.println("오류가 발생하여 메세지 업데이트가 되지 않았습니다.");
        }
    }

    @Override
    public void delete(Channel channel) {
        if (channelRepository.delete(channel)) {
            System.out.println("정상적으로 메세지가 삭제되었습니다.");
        }
        else {
            System.out.println("오류가 발생하여 메세지가 삭제되지 않았습니다.");
        }
    }
}
