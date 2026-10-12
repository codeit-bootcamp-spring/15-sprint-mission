package com.sprint.mission.discodeit.dto.Response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
        UUID id,
        String name,
        String description,
        ChannelType type,
        List<UserDto> participants,
        Instant lastMessageAt

) {
    public static ChannelDto from(Channel channel, List<User> users, Instant lastMessageAt) {
        List<UserDto> userDtos = users.stream().map(UserDto::from).toList();
        return new ChannelDto(channel.getId(), channel.getName(), channel.getDescription(), channel.getType(), userDtos, lastMessageAt);
    }

}
