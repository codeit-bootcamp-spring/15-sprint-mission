package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDto(

    @Schema(description = "메시지 ID")
    UUID id,

    @Schema(description = "메시지 생성 시각")
    Instant createdAt,

    @Schema(description = "메시지 최종 수정 시각")
    Instant updatedAt,

    @Schema(description = "메시지 내용")
    String content,

    @Schema(description = "메시지가 속한 채널 ID")
    UUID channelId,

    @Schema(description = "메시지 작성자 (탈퇴했으면 null)")
    UserDto author,

    @Schema(description = "첨부파일 목록")
    List<BinaryContentDto> attachments) {

}