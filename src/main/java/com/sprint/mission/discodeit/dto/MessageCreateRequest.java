package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MessageCreateRequest(

    @NotNull
    @Schema(description = "메시지를 작성할 채널 ID")
    UUID channelId,

    @NotNull
    @Schema(description = "메시지 작성자 ID")
    UUID authorId,

    @NotBlank
    @Schema(description = "메세지 내용", example = "HI~")
    String content) {

}
