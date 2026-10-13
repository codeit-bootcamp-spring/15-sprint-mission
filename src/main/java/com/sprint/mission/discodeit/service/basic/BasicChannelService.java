package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.Request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.Request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.Response.ChannelDto;
import com.sprint.mission.discodeit.dto.Response.UserDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public ChannelDto create(PublicChannelCreateRequest publicChannelCreateRequest) {
        Channel channel = new Channel(publicChannelCreateRequest.name(),publicChannelCreateRequest.description(), ChannelType.PUBLIC);
        channelRepository.save(channel);
        return toChannelResponse(channel);
    }

    @Override
    @Transactional
    public ChannelDto create(PrivateChannelCreateRequest privateChannelCreateRequest) {
        Channel channel = new Channel(null,null,ChannelType.PRIVATE);
        List<UUID> membersId = privateChannelCreateRequest.participantIds();
        channelRepository.save(channel);

        userRepository.findAllByIdIn(membersId).forEach(user ->
                readStatusRepository.save(
                        new ReadStatus(user, channel, Instant.now())
                )
        );


        return toChannelResponse(channel);
    }


    @Override
    public ChannelDto toChannelResponse(Channel channel){
        List<User> users = readStatusRepository.findAllByChannelId(channel.getId()).stream()
                .map(readStatus -> readStatus.getUser()).toList();


        Instant latestMessageAt = messageRepository.findLastMessageAt(channel.getId()).orElse(Instant.MIN);

        return  ChannelDto.from(channel,users,latestMessageAt);
    }



    @Override
    @Transactional(readOnly = true)
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<Channel> publicChannels = channelRepository.findAllByTypeEquals(ChannelType.PUBLIC);
        List<Channel> privateChannels = readStatusRepository.findAllByUserId(userId).stream()
                .map(readStatus -> readStatus.getChannel()).filter(channel -> channel.getType()==ChannelType.PRIVATE).toList();

        List<Channel> concatList = new ArrayList<>();
        List<ChannelDto> resultList;

        concatList.addAll(publicChannels);
        concatList.addAll(privateChannels);

        resultList = concatList.stream().map(this::toChannelResponse).toList();


        return resultList;
    }


    @Override
    @Transactional
    public ChannelDto update(UUID id, PublicChannelUpdateRequest publicChannelUpdateRequest) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("채널 id 없음 : " + id));
        if(channel.getType()==ChannelType.PRIVATE){
            throw new IllegalArgumentException("private채널은 업데이트할 수 없습니다.");
        }
        channel.update(publicChannelUpdateRequest.name(), publicChannelUpdateRequest.description());
        //channelRepository.save(channel);
        return toChannelResponse(channel);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!channelRepository.existsById(id)) {
            throw new NoSuchElementException("채널 id 없음 : " + id);
        }

        //messageRepository.deleteAllByChannel_Id(id);
        //readStatusRepository.deleteAllByChannel_Id(id);
        channelRepository.deleteById(id);
    }

}
