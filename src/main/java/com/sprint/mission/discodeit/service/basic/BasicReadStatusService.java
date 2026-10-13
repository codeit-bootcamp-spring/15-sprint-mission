package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.Request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.Response.ReadStatusDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;


    @Transactional
    @Override
    public ReadStatusDto create(ReadStatusCreateRequest readStatusCreateRequest) {

        Channel channel = channelRepository.findById(readStatusCreateRequest.channelId())
                .orElseThrow(() -> new NoSuchElementException("채널 id 없음 : "+ readStatusCreateRequest.channelId()));

        User user = userRepository.findById(readStatusCreateRequest.userId())
                .orElseThrow(() -> new NoSuchElementException("유저 id 없음 : "+ readStatusCreateRequest.userId()));

        if(readStatusRepository.existsByUserIdAndChannelId(readStatusCreateRequest.userId(),readStatusCreateRequest.channelId())){
            throw new IllegalArgumentException("해당 채널, 유저의 ReadStatus가 이미 존재합니다");
        }

        ReadStatus readStatus = new ReadStatus(user,channel,
                readStatusCreateRequest.lastReadAt());
        readStatusRepository.save(readStatus);
        return ReadStatusDto.from(readStatus);
    }



    @Transactional(readOnly = true)
    @Override
    public List<ReadStatusDto> findAllByUserId(UUID userId) {

        return readStatusRepository.findAllByUserId(userId).stream()
                .map(readStatus -> ReadStatusDto.from(readStatus)).toList();
    }


    @Transactional
    @Override
    public ReadStatusDto update(UUID id, ReadStatusUpdateRequest readStatusUpdateRequest) {
        ReadStatus readStatus = readStatusRepository.findById(id).orElseThrow(() -> new NoSuchElementException("ReadStatus id 없음 : " + id));
        readStatus.update(readStatusUpdateRequest.newLastReadAt());
        return ReadStatusDto.from(readStatus);
    }

}
