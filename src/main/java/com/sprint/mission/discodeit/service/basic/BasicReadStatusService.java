package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatus create(ReadStatusCreateRequest request) {
        // 1. User 미존재 검증
        if (!userRepository.existsById(request.userId())) {
            throw new NoSuchElementException("사용자가 존재하지 않습니다: " + request.userId());
        }

        // 2. Channel 미존재 검증
        if (!channelRepository.existsById(request.channelId())) {
            throw new NoSuchElementException("채널이 존재하지 않습니다: " + request.channelId());
        }

        // 3. 동일한 (Channel, User) 쌍 중복 등록 검증
        if (readStatusRepository.existsByUserIdAndChannelId(request.userId(), request.channelId())) {
            throw new IllegalArgumentException("해당 채널에 대한 사용자의 읽음 상태가 이미 존재합니다.");
        }

        ReadStatus readStatus = new ReadStatus(
                request.userId(),
                request.channelId(),
                request.lastReadAt()
        );

        return readStatusRepository.save(readStatus);
    }

    @Override
    public ReadStatus find(UUID id) {
        return readStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("ReadStatus가 존재하지 않습니다: " + id));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("사용자가 존재하지 않습니다: " + userId);
        }
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public ReadStatus update(ReadStatusUpdateRequest request) {
        ReadStatus readStatus = readStatusRepository.findById(request.id())
                .orElseThrow(() -> new NoSuchElementException("수정할 ReadStatus가 존재하지 않습니다: " + request.id()));

        // 엔티티 필드 수정
        readStatus.updateLastReadAt(request.lastReadAt());

        return readStatusRepository.save(readStatus);
    }

    @Override
    public void delete(UUID id) {
        if (!readStatusRepository.existsById(id)) {
            throw new NoSuchElementException("삭제할 ReadStatus가 존재하지 않습니다: " + id);
        }
        readStatusRepository.deleteById(id);
    }


}
