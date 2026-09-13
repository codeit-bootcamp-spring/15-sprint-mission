package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.Request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.Request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.Response.ChannelFindResponse;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
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



    @Override
    public Channel create(PublicChannelCreateRequest publicChannelCreateRequest) {
        Channel channel = new Channel(publicChannelCreateRequest.name(), ChannelType.PUBLIC);
        return channelRepository.save(channel);
    }

    @Override
    public Channel create(PrivateChannelCreateRequest privateChannelCreateRequest) {
        Channel channel = new Channel(null,ChannelType.PRIVATE);
        List<UUID> membersId = privateChannelCreateRequest.membersId();
        ReadStatus readStatus;
        channelRepository.save(channel);
        for (UUID entry : membersId){
            readStatus= new ReadStatus(entry, channel.getId());
            readStatusRepository.save(readStatus);
        }

        return channel;
    }

    @Override
    public ChannelFindResponse find(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("채널 id 없음 : " + id));

        return toChannelReadResponse(channel);
    }

    public ChannelFindResponse toChannelReadResponse(Channel channel){
        List<UUID> memberIds = readStatusRepository.findAllByChannelId(channel.getId())
                .stream()
                .map(ReadStatus::getUserId)
                .toList();

        Instant latestMessageAt = messageRepository.findAllByChannelId(channel.getId())
                .stream()
                .map(Message::getCreatedAt)
                .max(Instant::compareTo)
                .orElse(null);
        return new ChannelFindResponse(
                channel.getId(),
                channel.getCreatedAt(),
                channel.getUpdatedAt(),
                channel.getName(),
                channel.getChannelType(),
                memberIds,
                latestMessageAt

        );
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(ChannelUpdateRequest channelUpdateRequest) {
        Channel channel = channelRepository.findById(channelUpdateRequest.id())
                .orElseThrow(() -> new NoSuchElementException("채널 id 없음 : " + channelUpdateRequest.id()));
        if(channel.getChannelType()==ChannelType.PRIVATE){
            throw new IllegalArgumentException("private채널은 업데이트할 수 없습니다.");
        }
        channel.update(channelUpdateRequest.name());
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
            if(message.getBinaryIds()!=null){
                for(UUID entry : message.getBinaryIds()){
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
