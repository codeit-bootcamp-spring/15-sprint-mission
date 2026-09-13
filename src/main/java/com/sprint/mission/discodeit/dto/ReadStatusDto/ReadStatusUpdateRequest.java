package com.sprint.mission.discodeit.dto.ReadStatusDto;

import java.time.Instant;

public record ReadStatusUpdateRequest(

        Instant lastReadAt
) {}