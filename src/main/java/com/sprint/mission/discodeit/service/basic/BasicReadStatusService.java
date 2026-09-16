package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusDto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusDto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    @Override
    public ReadStatus create(ReadStatusCreateRequest request) {

        if (!channelRepository.existsById(request.channelId())) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }

        if (!userRepository.existsById(request.userId())) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        if (readStatusRepository
                .findByChannelIdAndUserId(request.channelId(), request.userId())
                .isPresent()) {
            throw new IllegalArgumentException("이미 ReadStatus가 존재합니다.");
        }

        ReadStatus readStatus =
                new ReadStatus(request.channelId(), request.userId());

        readStatusRepository.save(readStatus);

        return readStatus;
    }

    @Override
    public ReadStatus find(UUID id) {
        return readStatusRepository.findById(id).orElse(null);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public ReadStatus update(UUID id, ReadStatusUpdateRequest request) {

        ReadStatus readStatus =
                readStatusRepository.findById(id).orElse(null);

        if (readStatus == null) {
            return null;
        }

        readStatus.updateLastReadAt(request.lastReadAt());

        readStatusRepository.save(readStatus);

        return readStatus;
    }

    @Override
    public void delete(UUID id) {
        readStatusRepository.deleteById(id);
    }
}