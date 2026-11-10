package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PublicChannelUpdateRequest(

    @NotBlank(message = "채널 이름을 입력하세요.")
    String newName,

    String newDescription
) {

}
