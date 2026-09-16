package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.NoSuchElementException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatusDto create(ReadStatusCreateRequest request) {

        userRepository.findById(request.userId())
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 사용자입니다. ID: " + request.userId()
                        )
                );

        channelRepository.findById(request.channelId())
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 채널입니다. ID: " + request.channelId()
                        )
                );

        if (readStatusRepository
                .findByUserIdAndChannelId(request.userId(), request.channelId())
                .isPresent()) {
            throw new IllegalArgumentException(
                    "이미 존재하는 읽음 상태입니다."
            );
        }

        ReadStatus readStatus = new ReadStatus(
                request.userId(),
                request.channelId(),
                request.lastReadAt()
        );

        ReadStatus savedReadStatus = readStatusRepository.save(readStatus);

        return new ReadStatusDto(
                savedReadStatus.getId(),
                savedReadStatus.getUserId(),
                savedReadStatus.getChannelId(),
                savedReadStatus.getLastReadAt(),
                savedReadStatus.getCreatedAt(),
                savedReadStatus.getUpdatedAt()
        );
    }
    @Override
    public ReadStatusDto find(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 읽음 상태입니다. ID: " + id
                        )
                );

        return new ReadStatusDto(
                readStatus.getId(),
                readStatus.getUserId(),
                readStatus.getChannelId(),
                readStatus.getLastReadAt(),
                readStatus.getCreatedAt(),
                readStatus.getUpdatedAt()
        );
    }

    @Override
    public List<ReadStatusDto> findAllByUserId(UUID userId) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 사용자입니다. ID: " + userId
                        )
                );

        return readStatusRepository.findAllByUserId(userId)
                .stream()
                .map(readStatus -> new ReadStatusDto(
                        readStatus.getId(),
                        readStatus.getUserId(),
                        readStatus.getChannelId(),
                        readStatus.getLastReadAt(),
                        readStatus.getCreatedAt(),
                        readStatus.getUpdatedAt()
                ))
                .toList();
    }

    @Override
    public ReadStatusDto update(ReadStatusUpdateRequest request) {

        ReadStatus readStatus = readStatusRepository.findById(request.id())
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 읽음 상태입니다. ID: " + request.id()
                        )
                );

        readStatus.update(request.lastReadAt());

        ReadStatus savedReadStatus = readStatusRepository.save(readStatus);

        return new ReadStatusDto(
                savedReadStatus.getId(),
                savedReadStatus.getUserId(),
                savedReadStatus.getChannelId(),
                savedReadStatus.getLastReadAt(),
                savedReadStatus.getCreatedAt(),
                savedReadStatus.getUpdatedAt()
        );
    }

    @Override
    public void delete(UUID id) {

        readStatusRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "존재하지 않는 읽음 상태입니다. ID: " + id
                        )
                );

        readStatusRepository.deleteById(id);
    }

}

