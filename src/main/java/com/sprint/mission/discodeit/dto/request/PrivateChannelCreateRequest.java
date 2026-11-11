package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(
        @NotEmpty(message = "비공개 채널의 구성원이 없습니다.")
        List<@NotNull UUID> participantIds
) {

}
