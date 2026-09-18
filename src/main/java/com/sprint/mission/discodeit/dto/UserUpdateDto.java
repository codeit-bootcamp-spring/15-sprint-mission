package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UserUpdateDto(UUID id,
                            String username,
                            String email,
                            String password,
                            BinaryContentCreateDto newProfileImage) {

}
