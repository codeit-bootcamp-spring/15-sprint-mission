package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageCreateDto;
import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {

    Message create(MessageCreateDto dto);
    Message find(UUID id);
    List<Message> findAllByChannelId(UUID channelId);
    // update(UUID, String) 대신 DTO를 받는 형태로 수정
    Message update(MessageUpdateDto dto);
    void delete(UUID id);
}