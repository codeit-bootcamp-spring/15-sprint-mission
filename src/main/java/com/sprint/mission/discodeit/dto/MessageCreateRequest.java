package com.sprint.mission.discodeit.dto;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;

public record MessageCreateRequest(
    UUID channelId,
    UUID authorId,
    UUID userId, // 혹시 프론트가 userId로 보낼 경우를 대비
    String content
) {

}