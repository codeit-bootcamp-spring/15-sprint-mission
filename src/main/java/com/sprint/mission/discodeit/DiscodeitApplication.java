package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {

    static UserResponse setupUser(UserService userService) {
        System.out.println("=== User CRUD ===");

        UserResponse user1 = userService.create(
                new UserCreateRequest("민영", "minyeong@naver.com", "1234a"), null);
        UserResponse user2 = userService.create(
                new UserCreateRequest("탱이", "taeng@naver.com", "5678a"), null);
        System.out.println("등록: " + user1.userName() + ", " + user2.userName());

        UserResponse foundUser = userService.read(user1.id());
        System.out.println("단건 조회: " + foundUser.userName());
        System.out.println("다건 조회:");
        for (UserResponse user : userService.readAll()) {
            System.out.println("- " + user.userName());
        }

        userService.update(user1.id(), new UserUpdateRequest("민영(수정됨)", null, null), null);
        System.out.println("수정 완료");
        UserResponse updated = userService.read(user1.id());
        System.out.println("수정된 데이터 조회: " + updated.userName());
        System.out.println("이메일 확인: " + updated.email());

        userService.delete(user2.id());
        System.out.println("삭제 완료");
        System.out.println("삭제 후 조회:");
        for (UserResponse user : userService.readAll()) {
            System.out.println("- " + user.userName());
        }

        return updated;
    }

    static ChannelResponse setupChannel(ChannelService channelService, UUID userId) {
        System.out.println("=== Channel CRUD ===");

        ChannelResponse channel1 = channelService.createPublicChannel(
                new PublicChannelCreateRequest("공부", "공부 채널"));
        ChannelResponse channel2 = channelService.createPublicChannel(
                new PublicChannelCreateRequest("수다", "수다 채널"));
        ChannelResponse channel3 = channelService.createPublicChannel(
                new PublicChannelCreateRequest("여행", "여행 채널"));
        System.out.println("등록: " + channel1.channelName() + ", " + channel2.channelName() + ", " + channel3.channelName());

        System.out.println("단건 조회: " + channelService.read(channel1.id()).channelName());

        System.out.println("다건 조회:");
        for (ChannelResponse channel : channelService.readAllByUserId(userId)) {
            System.out.println("- " + channel.channelName());
        }

        channelService.update(channel1.id(), new ChannelUpdateRequest("공부(수정됨)", null));
        System.out.println("수정 완료");
        System.out.println("수정된 데이터 조회: " + channelService.read(channel1.id()).channelName());

        channelService.delete(channel2.id());
        System.out.println("삭제 완료");
        System.out.println("삭제 후 조회:");
        for (ChannelResponse channel : channelService.readAllByUserId(userId)) {
            System.out.println("- " + channel.channelName());
        }

        return channel1;
    }

    static void messageCreateTest(MessageService messageService, ChannelResponse channel, UserResponse author) {
        System.out.println("=== Message CRUD ===");

        MessageResponse message1 = messageService.create(
                new MessageCreateRequest("안녕하세요", channel.id(), author.id(), null));
        MessageResponse message2 = messageService.create(
                new MessageCreateRequest("환영합니다", channel.id(), author.id(), null));
        System.out.println("등록: " + message1.contents() + ", " + message2.contents());

        System.out.println("단건 조회: " + messageService.read(message1.id()).contents());

        System.out.println("다건 조회:");
        for (MessageResponse message : messageService.readAllByChannelId(channel.id())) {
            System.out.println("- " + message.contents());
        }

        messageService.update(message1.id(), new MessageUpdateRequest("안녕하세요(수정됨)"));
        System.out.println("수정 완료");
        System.out.println("수정된 데이터 조회: " + messageService.read(message1.id()).contents());

        messageService.delete(message1.id());
        System.out.println("삭제 완료");
        System.out.println("삭제 후 조회:");
        for (MessageResponse message : messageService.readAllByChannelId(channel.id())) {
            System.out.println("- " + message.contents());
        }
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        UserResponse user = setupUser(userService);
        ChannelResponse channel = setupChannel(channelService, user.id());
        messageCreateTest(messageService, channel, user);
    }
}