package com.sprint.mission.discodeit.dto.Request;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
        UUID userId,
        UUID channelId,
        Instant lastReadAt// todo 아직 다른 코드에 적용 안함 + 업데이트리퀘스트도 만들 것
) {
}
