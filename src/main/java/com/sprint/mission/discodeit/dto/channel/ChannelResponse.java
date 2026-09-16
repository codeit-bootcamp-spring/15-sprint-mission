package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponse( // find/findAll에 최근 메시지 시각과 PRIVATE 참여자 ID 포함
                               UUID id,
                               ChannelType type,
                               String name,
                               Instant lastMessageAt,
                               List<UUID> userIds
) {
}
