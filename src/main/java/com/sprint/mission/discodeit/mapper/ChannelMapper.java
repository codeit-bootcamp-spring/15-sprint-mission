package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.Response.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = UserMapper.class
)
public interface ChannelMapper {

    @Mapping(target = "participants", source = "users")
    @Mapping(target = "lastMessageAt", source = "lastMessageAt")
    ChannelDto toDto(
            Channel channel,
            List<User> users,
            Instant lastMessageAt
    );
}