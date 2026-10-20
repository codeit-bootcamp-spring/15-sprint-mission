package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.Request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.Request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.Response.ChannelDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserRepository userRepository;
    private final ChannelMapper channelMapper;


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
        List<User> users = readStatusRepository.findAllByChannelIdJoinUser(channel.getId()).stream()
                .map(readStatus -> readStatus.getUser()).toList();


        Instant lastMessageAt = messageRepository.findLastMessageAt(channel.getId()).orElse(Instant.now());

        return  channelMapper.toDto(channel,users,lastMessageAt);
    }



    @Override
    @Transactional(readOnly = true)
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<Channel> channels = channelRepository.findAllVisibleToUser(userId,
                ChannelType.PUBLIC,ChannelType.PRIVATE);

        if (channels.isEmpty()) {
            return List.of();
        }

        List<UUID> channelIds = channels.stream()
                .map(Channel::getId)
                .toList();

        Map<UUID, List<User>> usersByChannelId =
                readStatusRepository.findAllByChannelIdsJoinUserDetails(channelIds)
                        .stream()
                        .collect(Collectors.groupingBy(
                                rs -> rs.getChannel().getId(),
                                Collectors.mapping(ReadStatus::getUser, Collectors.toList())
                        ));

        Map<UUID, Instant> latestMessageAtByChannelId =
                messageRepository.findLastMessageAtByChannelIds(channelIds)
                        .stream()
                        .collect(Collectors.toMap(
                                ChannelLastMessageAt::getChannelId,
                                ChannelLastMessageAt::getLatestMessageAt
                        ));

        return channels.stream()
                .map(channel -> channelMapper.toDto(
                        channel,
                        usersByChannelId.getOrDefault(channel.getId(), List.of()),
                        latestMessageAtByChannelId.getOrDefault(
                                channel.getId(), Instant.now()
                        )
                ))
                .toList();
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
