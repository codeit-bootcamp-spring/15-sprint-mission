package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public Channel createPublicChannel(String name, String description) {
        Channel channel = new Channel(ChannelType.PUBLIC, name, description);
        List<Channel> channels = channelRepository.findAll();

        // 중복 여부 확인
        for (Channel c : channels) {
            if (c.getName().equals(name)) throw new IllegalArgumentException("채널 이름은 중복될 수 없습니다.");
        }

        boolean isCreated = channelRepository.create(channel);
        if (!isCreated) throw new IllegalStateException("채널 저장 중 오류가 발생했습니다.");

        return channel;

    }

    @Override
    public Channel createPrivateChannel(List<User> userIds) {
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);

        boolean isCreated = channelRepository.create(channel);
        if (!isCreated) throw new IllegalStateException("채널 저장 중 오류가 발생했습니다.");

        for (User u : userIds) {
            ReadStatus readStatus = new ReadStatus(u.getId(), channel.getId());
            readStatusRepository.save(readStatus);
        }
        return channel;

    }

    @Override
    public ChannelResponse find(UUID channelId) throws IllegalArgumentException {
        Channel channel = channelRepository.find(channelId);
        if (channel == null) throw new IllegalArgumentException("채널을 찾지 못했습니다");

        List<Message> message = messageRepository.findByChannelId(channelId);
        if (message == null || message.isEmpty()) throw new IllegalArgumentException("메시지를 찾지 못했습니다");

        ChannelResponse channelResponse;
        Instant latestTime = messageRepository.findByChannelId(channelId).stream()
                .map(Message::getCreatedAt)
                .max(Comparator.naturalOrder())
                .orElse(null);

        if (channel.getType().equals(ChannelType.PRIVATE)) { // Private 채널일 경우
            // readStatus 에서 프라이빗 채널에 속한 UserID를 가져오는 과정이 필요함.
            List<ReadStatus> readStatus = readStatusRepository.findAllByChannelId(channelId);
            if (readStatus == null) throw new IllegalArgumentException("읽을 파일이 없습니다.");

            List<UUID> users = readStatus.stream().map(ReadStatus::getUserId).toList(); //UserId를 배열로

            channelResponse = new ChannelResponse(
                    channel.getId(),
                    channel.getType(),
                    channel.getName(),
                    latestTime,
                    users);
        }
        else {
            channelResponse = new ChannelResponse(
                    channel.getId(),
                    channel.getType(),
                    channel.getName(),
                    latestTime,
                    null); // new ArrayList<>() 도 고려해볼만 함.
        }

        return channelResponse;
    }

    @Override
    public ChannelResponse findByChannelName(String channelName) throws IllegalArgumentException {
        Channel channel = channelRepository.findByChannelName(channelName);
        if (channel == null) throw new IllegalArgumentException("채널을 찾지 못했습니다");
        UUID channelId = channel.getId();

        List<Message> message = messageRepository.findByChannelId(channelId);
        if (message == null || message.isEmpty()) throw new IllegalArgumentException("메시지를 찾지 못했습니다");

        ChannelResponse channelResponse;
        Instant latestTime = messageRepository.findByChannelId(channelId).stream()
                .map(Message::getCreatedAt)
                .max(Comparator.naturalOrder())
                .orElse(null);

        if (channel.getType().equals(ChannelType.PRIVATE)) { // Private 채널일 경우
            // readStatus 에서 프라이빗 채널에 속한 UserID를 가져오는 과정이 필요함.
            List<ReadStatus> readStatus = readStatusRepository.findAllByChannelId(channelId);
            if (readStatus == null) throw new IllegalArgumentException("읽을 파일이 없습니다.");

            List<UUID> users = readStatus.stream().map(ReadStatus::getUserId).toList(); //UserId를 배열로

            channelResponse = new ChannelResponse(
                    channel.getId(),
                    channel.getType(),
                    channel.getName(),
                    latestTime,
                    users);
        }
        else {
            channelResponse = new ChannelResponse(
                    channel.getId(),
                    channel.getType(),
                    channel.getName(),
                    latestTime,
                    null); // new ArrayList<>() 도 고려해볼만 함.
        }

        return channelResponse;
    }

    @Override
    public List<ChannelResponse> findAllByUserId(UUID userId) {
        List<ChannelResponse> result = new ArrayList<>();
        List<ReadStatus> readStatuses = readStatusRepository.findAllByUserId(userId);
        readStatuses.stream().map(ReadStatus::getChannelId).toList();

        for(ReadStatus r: readStatuses) {
            Channel channel = channelRepository.find(r.getChannelId());
            Instant latestTime = messageRepository.findByChannelId(channel.getId()).stream()
                    .map(Message::getCreatedAt)
                    .max(Comparator.naturalOrder())
                    .orElse(null);

            if (channel.getType().equals(ChannelType.PUBLIC)) {
                result.add(new ChannelResponse(r.getChannelId(), channel.getType(), channel.getName(), latestTime, null));
            }
            else {
                List<ReadStatus> readStatus = readStatusRepository.findAllByChannelId(channel.getId());
                if (readStatus == null) throw new IllegalArgumentException("읽을 파일이 없습니다.");

                List<UUID> users = readStatus.stream().map(ReadStatus::getUserId).toList();
                result.add(new ChannelResponse(r.getChannelId(), channel.getType(), channel.getName(), latestTime, users));
            }

        }

        return result;
    }

    @Override
    public void update(ChannelUpdateRequest cu) throws IllegalArgumentException, IllegalStateException{
        Channel channel = channelRepository.find(cu.id());
        if (channel == null) throw new IllegalArgumentException("채널을 찾지 못했습니다");
        if (channel.getType().equals(ChannelType.PRIVATE)) throw new IllegalStateException("Private 채널은 수정이 불가능합니다.");

        channel.setName(cu.name());
        channel.setDescription(cu.description());
        channel.autoSetUpdatedAt();

        boolean isUpdated = channelRepository.update(channel);
        if (!isUpdated) throw new NoSuchElementException("오류가 발생하여 메세지 업데이트가 되지 않았습니다.");
        System.out.println("정상적으로 메시지가 업데이트 되었습니다.");
    }

    @Override
    public void delete(UUID channelId) throws IllegalArgumentException{
        Channel channel = channelRepository.find(channelId);
        List<ReadStatus> readStatus = readStatusRepository.findAllByChannelId(channelId);
        List<Message> message = messageRepository.findByChannelId(channelId);
        if (channel == null || readStatus == null || message == null) {
            throw new IllegalArgumentException("데이터를 조회하는 중 비정상적인 null 상태가 감지되어 삭제가 불가능합니다.");
        }

        for (ReadStatus r : readStatus) {
            if (!readStatusRepository.delete(r.getId())) {
                throw new IllegalArgumentException("삭제 도중 오류가 발생하여 메세지가 삭제되지 않았습니다.");
            }
        }

        for (Message m : message) {
            if (!messageRepository.delete(m.getId())) {
                throw new IllegalArgumentException("삭제 도중 오류가 발생하여 메세지가 삭제되지 않았습니다.");
            }
        }


        if (channelRepository.delete(channelId)) {
            System.out.println("정상적으로 메세지가 삭제되었습니다.");
        }
        else {
            System.out.println("오류가 발생하여 메세지가 삭제되지 않았습니다.");
        }
    }
}
