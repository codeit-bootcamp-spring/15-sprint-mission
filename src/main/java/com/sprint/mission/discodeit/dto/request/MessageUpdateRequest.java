package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MessageUpdateRequest(

    @NotBlank(message = "newContent는 비어 있을 수 없습니다.")
    String newContent
) {

}
