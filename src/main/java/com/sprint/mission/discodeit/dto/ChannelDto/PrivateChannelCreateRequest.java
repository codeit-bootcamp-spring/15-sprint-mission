package com.sprint.mission.discodeit.dto.ChannelDto;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(

        UUID ownerId,
        List<UUID> userIds

) {
}