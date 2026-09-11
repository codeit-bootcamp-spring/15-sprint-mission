package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;


    @Override
    public ReadStatus create(ReadStatusCreateRequest readStatusCreateRequest) {
        if(!userRepository.existsById(readStatusCreateRequest.userId())){
            throw new IllegalArgumentException("존재하지 않는 유저 id : "+ readStatusCreateRequest.userId());
        }

        if(!channelRepository.existsById(readStatusCreateRequest.channelId())){
            throw new IllegalArgumentException("존재하지 않는 채널 id : "+ readStatusCreateRequest.channelId());
        }

        if(readStatusRepository.existsByUserAndChannel(readStatusCreateRequest.userId(),readStatusCreateRequest.channelId())){
            throw new IllegalArgumentException("해당 채널, 유저의 ReadStatus가 이미 존재합니다");
        }

        ReadStatus readStatus = new ReadStatus(readStatusCreateRequest.userId(),readStatusCreateRequest.channelId());
        return readStatusRepository.save(readStatus);
    }

    @Override
    public ReadStatus find(UUID id) {
        return readStatusRepository.findById(id).orElseThrow(() -> new NoSuchElementException("ReadStatus id 없음 : " + id));

    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public ReadStatus update(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(id).orElseThrow(() -> new NoSuchElementException("ReadStatus id 없음 : " + id));
        readStatus.update();
        return readStatusRepository.save(readStatus);
    }

    @Override
    public void delete(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(id).orElseThrow(() -> new NoSuchElementException("ReadStatus id 없음 : " + id));
        readStatusRepository.deleteById(id);

    }
}
