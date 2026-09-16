package com.sprint.mission.discodeit.dto.Request;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(
        List<UUID> membersId
) {

}
