package com.sprint.mission.discodeit.dto.ReadStatusDto;

import java.util.UUID;

public record ReadStatusCreateRequest(

        UUID channelId,
        UUID userId
) {}