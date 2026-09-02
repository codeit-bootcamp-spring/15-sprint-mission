package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    @Override
    public Channel create(ChannelCreateRequest cr) {
        Channel channel = new Channel(cr.type(), cr.name(), cr.description());
        List<Channel> channels = channelRepository.findAll();

        // 중복 여부 확인
        for (Channel c : channels) {
            if (c.getName().equals(cr.name())) {
                System.out.println("채널 이름은 중복될 수 없습니다.");
                return null;
            }
        }

        if (channelRepository.create(channel)) {
            return channel;
        }
        return null;
    }

    @Override
    public Channel find(UUID id) {
        return null;
    }

    @Override
    public Channel findByChannelName(String channelName) {
        return null;
    }

    @Override
    public List<Channel> readAll() {
        return channelRepository.findAll();
    }

    @Override
    public void update(UUID id, ChannelUpdateRequest cu) {
        Channel channel = channelRepository.find(id);
        if (channel == null) {
            System.out.println("저장소에서 해당 채널을 찾을 수 없습니다.");
            return;
        }

        channel.setType(cu.type());
        channel.setName(cu.name());
        channel.setDescription(cu.description());
        channel.autoSetUpdatedAt();

        if (channelRepository.update(channel)) {
            System.out.println("정상적으로 메세지 업데이트가 되었습니다.");
        }
        else {
            System.out.println("오류가 발생하여 메세지 업데이트가 되지 않았습니다.");
        }
    }

    @Override
    public void delete(UUID id) {
        if (channelRepository.delete(id)) {
            System.out.println("정상적으로 메세지가 삭제되었습니다.");
        }
        else {
            System.out.println("오류가 발생하여 메세지가 삭제되지 않았습니다.");
        }
    }
}
