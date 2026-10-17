package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.base.Channel.ChannelType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(

    @Schema(description = "채널 ID")
    UUID id,

    @Schema(description = "채널 유형 (PUBLIC, PRIVATE)")
    ChannelType type,

    @Schema(description = "채널 이름 (PRIVATE은 null)")
    String name,

    @Schema(description = "채널 소개 (PRIVATE은 null)")
    String description,

    @Schema(description = "채널 참여자 목록")
    List<UserDto> participants,

    @Schema(description = "채널의 가장 최근 메시지 시각 (메시지가 없으면 null)")
    Instant lastMessageAt) {

}