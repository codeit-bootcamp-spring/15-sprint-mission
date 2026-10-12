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
    public Channel create(PublicChannelCreateRequest publicChannelCreateRequest) {
        Channel channel = new Channel(publicChannelCreateRequest.name(),publicChannelCreateRequest.description(), ChannelType.PUBLIC);
        return channelRepository.save(channel);
    }

    @Override
    public Channel create(PrivateChannelCreateRequest privateChannelCreateRequest) {
        Channel channel = new Channel(null,null,ChannelType.PRIVATE);
        List<UUID> membersId = privateChannelCreateRequest.participantIds();
        ReadStatus readStatus;
        channelRepository.save(channel);
        for (UUID entry : membersId){
            User user=userRepository.findById(entry).orElseThrow(() -> new NoSuchElementException("유저 id 찾을 수 없음 : "+ entry));
            readStatus= new ReadStatus(user, channel,Instant.MIN);
            readStatusRepository.save(readStatus);
        }

        return channel;
    }


    @Override
    public ChannelDto toChannelResponse(Channel channel){
        List<UserDto> UserDtos = readStatusRepository.findAllByChannelId(channel.getId())
                .stream()
                .map(readStatus -> readStatus.getUser())
                .map(user -> UserRepository.)
                .toList();

        Instant latestMessageAt = messageRepository.findAllByChannelId(channel.getId())
                .stream()
                .map(Message::getCreatedAt)
                .max(Instant::compareTo)
                .orElse(null);
        return new ChannelDto(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                channel.getType(),
                UserDtos,
                latestMessageAt

        );
    }



    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<Channel> publicChannels = channelRepository.findAll().stream().filter(channel -> channel.getType()==ChannelType.PUBLIC).toList();
        List<UUID> privateChannelIdList = readStatusRepository.findAllByUserId(userId).stream()
                .map(readStatus -> readStatus.getChannelId()).toList();
        List<Channel> privateChannels = new ArrayList<>();
        for(UUID id : privateChannelIdList){
            if(channelRepository.existsById(id)){
                privateChannels.add(channelRepository.findById(id).get());
            }
        }

        privateChannels= privateChannels.stream().filter(channel -> channel.getType()==ChannelType.PRIVATE).toList();

        List<Channel> concatList = new ArrayList<>();
        List<ChannelDto> resultList;

        concatList.addAll(publicChannels);
        concatList.addAll(privateChannels);

        resultList = concatList.stream().map(this::toChannelResponse).toList();


        return resultList;
    }


    @Override
    public Channel update(UUID id, PublicChannelUpdateRequest publicChannelUpdateRequest) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("채널 id 없음 : " + id));
        if(channel.getType()==ChannelType.PRIVATE){
            throw new IllegalArgumentException("private채널은 업데이트할 수 없습니다.");
        }
        channel.update(publicChannelUpdateRequest.name(), publicChannelUpdateRequest.description());
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID id) {
        if (!channelRepository.existsById(id)) {
            throw new NoSuchElementException("채널 id 없음 : " + id);
        }

        List<Message> messages =messageRepository.findAllByChannelId(id);
        List<UUID> readStatus = readStatusRepository.findAllByChannelId(id).stream().map(r -> r.getId()).toList();

        for(Message message : messages){
            if(message.getAttachmentIds()!=null){
                for(UUID entry : message.getAttachmentIds()){
                    if (binaryContentRepository.existsById(entry)) {
                        binaryContentRepository.deleteById(entry);
                    }
                }
            }
            messageRepository.deleteById(message.getId());
        }


        for(UUID readStatusId : readStatus){
            readStatusRepository.deleteById(readStatusId);
        }
        channelRepository.deleteById(id);
    }

}
