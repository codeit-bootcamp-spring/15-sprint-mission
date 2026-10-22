package com.sprint.mission.discodeit.dto.Response;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String content,
        UUID channelId,
        UserDto author,
        List<BinaryContentDto> attachments
) {

    public static MessageDto from(Message message) {
        User author = message.getAuthor();

        List<BinaryContentDto> binaryContentDtos = message.getAttachments().stream().map(BinaryContentDto::from).toList();

        return new MessageDto(message.getId(),
                message.getCreatedAt(), message.getUpdatedAt(),
                message.getContent(), message.getChannel().getId(),
                author == null ? null : UserDto.from(author),
                binaryContentDtos);
    }

}
