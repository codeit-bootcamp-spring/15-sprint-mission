package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.exception.ConflictException;
import com.sprint.mission.discodeit.dto.exception.NotFoundException;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatus create(ReadStatusCreateRequest rcr) {
        if (userRepository.find(rcr.userId()) == null || channelRepository.find(rcr.channelId()) == null) {
            throw new NotFoundException("채널이나 유저가 존재하지 않습니다.");
        }
        if (readStatusRepository.isAlreadyExist(rcr.userId(), rcr.channelId()) != null) {
            throw new ConflictException("이미 동일한 객체가 존재합니다.");
        }

        ReadStatus readStatus = new ReadStatus(rcr.userId(), rcr.channelId());
        readStatusRepository.save(readStatus);

        return readStatus;
    }

    @Override
    public ReadStatus find(UUID id) {
        return readStatusRepository.find(id);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public void update(ReadStatusUpdateRequest rur) {
        List<ReadStatus> readStatuses = readStatusRepository.findAllByChannelId(rur.channelId());
        if (readStatuses.isEmpty()) {
            throw new NotFoundException("업데이트할 객체를 찾을 수 없습니다.");
        }

        ReadStatus latestReadStatus = readStatuses.stream()
                .filter(rs -> rs.getLastReadAt() != null)
                .max(Comparator.comparing(ReadStatus::getLastReadAt))
                .orElseThrow(() -> new NotFoundException("읽음 기록이 있는 사용자가 없습니다."));


        latestReadStatus.autoSetUpdatedAt();

        readStatusRepository.save(latestReadStatus);
    }

    @Override
    public void delete(UUID id) {
        ReadStatus readStatus = readStatusRepository.find(id);
        if (readStatus == null) {
            throw new NotFoundException("삭제할 객체를 찾을 수 없습니다.");
        }

        readStatusRepository.delete(id);
    }
}
