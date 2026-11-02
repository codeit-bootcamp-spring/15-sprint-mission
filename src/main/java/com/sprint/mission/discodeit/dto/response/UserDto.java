package com.sprint.mission.discodeit.dto.response;


import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;
public record UserDto(
        UUID id,
        String username,
        String email,
        BinaryContentDto profile,
        boolean online
) {


}
