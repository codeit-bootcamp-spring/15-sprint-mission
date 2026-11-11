package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublicChannelUpdateRequest(
        @NotBlank(message = "공개 채널의 채널명은 필수입니다.")
        @Size(max = 100, message = "채널명은 최대 100글자 입니다.")
        String name,

        @Size(max = 500, message = "설명은 최대 500글자 입니다.")
        String description


) {
}
