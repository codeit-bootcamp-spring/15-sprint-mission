package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
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

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatusResponse create(ReadStatusCreateRequest request) {
        if (userRepository.read(request.userId()) == null) {
            throw new NoSuchElementException("존재하지 않는 유저");
        }

        if (channelRepository.read(request.channelId()) == null) {
            throw new NoSuchElementException("존재하지 않는 채널");
        }

        if (readStatusRepository.existsByUserIdAndChannelId(request.userId(), request.channelId())) {
            throw new IllegalStateException("이미 존재하는 데이터: " + request.userId() + ", " + request.channelId());
        }

        ReadStatus readStatus = new ReadStatus(request.userId(), request.channelId(), request.lastReadAt());

        return toResponse(readStatusRepository.save(readStatus));
    }

    @Override
    public ReadStatusResponse read(UUID id) {
        ReadStatus readStatus = readStatusRepository.read(id);
        if (readStatus == null) {
            throw new NoSuchElementException("존재하지 않는 읽음 상태");
        }
        return toResponse(readStatus);
    }

    @Override
    public List<ReadStatusResponse> readAllByUserId(UUID userId) {
        return readStatusRepository.readAllByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ReadStatusResponse update(UUID id, ReadStatusUpdateRequest request) {
        ReadStatus readStatus = readStatusRepository.read(id);
        if (readStatus == null) {
            throw new NoSuchElementException("존재하지 않는 읽음 상태");
        }
        readStatus.update(request.lastReadAt());

        return toResponse(readStatusRepository.save(readStatus));
    }

    @Override
    public void delete(UUID id) {
        readStatusRepository.delete(id);
    }

    private ReadStatusResponse toResponse(ReadStatus readStatus) {
        return new ReadStatusResponse(
                readStatus.getId(),
                readStatus.getUserId(),
                readStatus.getChannelId(),
                readStatus.getLastReadAt(),
                readStatus.getCreatedAt(),
                readStatus.getUpdatedAt()
        );
    }
}