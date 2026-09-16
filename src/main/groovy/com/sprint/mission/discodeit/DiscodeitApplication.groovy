package com.sprint.mission.discodeit

import com.sprint.mission.discodeit.dto.ChannelDto
import com.sprint.mission.discodeit.dto.MessageCreateRequest
import com.sprint.mission.discodeit.dto.MessageDto
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest
import com.sprint.mission.discodeit.dto.UserCreateRequest
import com.sprint.mission.discodeit.dto.UserDto
import com.sprint.mission.discodeit.service.ChannelService
import com.sprint.mission.discodeit.service.MessageService
import com.sprint.mission.discodeit.service.UserService
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ConfigurableApplicationContext

import java.util.Collections

@SpringBootApplication
class DiscodeitApplication {

    static UserDto setupUser(UserService userService) {
        UserCreateRequest request = new UserCreateRequest(
                "woody",
                "woody@codeit.com",
                "password123",
                null
        )

        return userService.create(request)
    }

    static ChannelDto setupChannel(ChannelService channelService) {
        PublicChannelCreateRequest request =
                new PublicChannelCreateRequest("공지")

        return channelService.createPublic(request)
    }

    static void messageCreateTest(
            MessageService messageService,
            ChannelDto channel,
            UserDto author
    ) {
        MessageCreateRequest request = new MessageCreateRequest(
                "안녕하세요.",
                channel.id(),
                author.id(),
                Collections.emptyList()
        )

        MessageDto message = messageService.create(request)

        System.out.println("메시지 생성: " + message.id())
    }

    static void main(String[] args) {

        ConfigurableApplicationContext context =
                SpringApplication.run(DiscodeitApplication, args)

        UserService userService =
                context.getBean(UserService)

        ChannelService channelService =
                context.getBean(ChannelService)

        MessageService messageService =
                context.getBean(MessageService)

        UserDto user = setupUser(userService)
        ChannelDto channel = setupChannel(channelService)

        messageCreateTest(
                messageService,
                channel,
                user
        )
    }
}

