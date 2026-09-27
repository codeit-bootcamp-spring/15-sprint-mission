package com.sprint.mission.discodeit.dto;



public record UserUpdateDto(
                            String username,
                            String email,
                            String password,
                            BinaryContentCreateDto newProfileImage) {

}
