package com.sprint.mission.discodeit.dto.Response;


import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;
public record UserDto(
        UUID id,
        String username,
        String email,
        BinaryContentDto profile,
        boolean online
) {
    public static UserDto from(User user){
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(),
                user.getProfile() == null ? null : BinaryContentDto.from(user.getProfile()),
                user.getStatus() != null && user.getStatus().isOnline());
    }

}
