package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(

    @NotEmpty(message = "참여자를 최소 1명 이상 선택해야합니다.")
    List<UUID> participantIds
) {

}
