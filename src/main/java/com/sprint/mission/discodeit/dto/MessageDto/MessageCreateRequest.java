package com.sprint.mission.discodeit.dto.MessageDto;

import java.util.UUID;

public record MessageCreateRequest(

        UUID channelId,
        UUID authorId,
        String content
) {}