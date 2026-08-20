package com.sprint.mission.discodeit.entity.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.service.ChannelService;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;

import java.io.Serial;
import java.util.List;
import java.util.UUID;

public class BasicChannelService implements ChannelService {

    // private JCFChannelRepository jcfChannelRepository;
    private final FileChannelRepository fileChannelRepository;
    @Serial
    private static final long serialVersionUID = 1L;

    public BasicChannelService() {
        this.fileChannelRepository = new FileChannelRepository();
        // this.data = new LinkedHashMap<>();
    }

    @Override
    public Channel createChannel(String name, String topic) {
        // 새로운 채널을 생성해주고, 그 생성한 채널을 파일에 직렬화하여 넣어주는 로직.
        Channel channel = new Channel(name, topic);
        fileChannelRepository.save(channel);
        return channel;
    }

    @Override
    public Channel getById(UUID id) {
        return fileChannelRepository.load(id);
    } // 저장 로직

    @Override
    public List<Channel> readAll() {
        return fileChannelRepository.loadValue();
    } // 저장 로직

    @Override
    public Channel update(UUID id, String name, String topic) {
        Channel channel = fileChannelRepository.load(id);
        channel.update(name, topic); // 조회 // 저장 로직
        fileChannelRepository.save(channel); // 추가 // 저장 로직

        return channel;
    }

    @Override
    public boolean deletebyID(UUID id) {
        boolean result = fileChannelRepository.delete(id); // 저장 로직
        // 삭제 여부를 result에 저장
        return result;
    }
}
