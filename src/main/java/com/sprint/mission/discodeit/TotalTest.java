package com.sprint.mission.discodeit;

//package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.Request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.Request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.Request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.Request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.Response.ChannelFindResponse;
import com.sprint.mission.discodeit.dto.Response.UserFindResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.NitroLevel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class TotalTest {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(TotalTest.class, args);
        try {
            UserService userService =
                    context.getBean(UserService.class);

            ChannelService channelService =
                    context.getBean(ChannelService.class);

            MessageService messageService =
                    context.getBean(MessageService.class);

            System.out.println("========== 테스트 시작 ==========");

            User user = testUser(userService);

            Channel channel = testChannel(channelService);

            Message message = testMessage(
                    messageService,
                    channel,
                    user
            );

            testMessageUpdate(
                    messageService,
                    message
            );

            testMessageDelete(
                    messageService,
                    message
            );

            testChannelDelete(
                    channelService,
                    channel
            );

            System.out.println("\n========== 모든 테스트 완료 ==========");

        } finally {
            context.close();
        }
    }

    private static User testUser(UserService userService) {
        System.out.println("\n[1] 사용자 생성 및 조회 테스트");

        String uniqueValue = UUID.randomUUID().toString();

        UserCreateRequest request =
                new UserCreateRequest(
                        "woody-" + uniqueValue + "@codeit.com",
                        "woody1234",
                        "woody-" + uniqueValue,
                        NitroLevel.BASIC,
                        Optional.empty()
                );

        User user = userService.create(request);

        if (user == null || user.getId() == null) {
            throw new AssertionError("사용자 생성에 실패했습니다.");
        }

        System.out.println("사용자 생성 성공");
        System.out.println("사용자 ID: " + user.getId());

        UserFindResponse foundUser =
                userService.find(user.getId());

        if (!foundUser.id().equals(user.getId())) {
            throw new AssertionError(
                    "사용자 조회 결과의 ID가 일치하지 않습니다."
            );
        }

        System.out.println("사용자 조회 성공");

        return user;
    }

    private static Channel testChannel(ChannelService channelService) {
        System.out.println("\n[2] 공개 채널 생성 및 조회 테스트");

        String channelName =
                "공지-" + UUID.randomUUID();

        PublicChannelCreateRequest request =
                new PublicChannelCreateRequest(channelName);

        Channel channel =
                channelService.create(request);

        if (channel == null || channel.getId() == null) {
            throw new AssertionError("채널 생성에 실패했습니다.");
        }

        System.out.println("채널 생성 성공");
        System.out.println("채널 ID: " + channel.getId());

        ChannelFindResponse foundChannel =
                channelService.find(channel.getId());

        if (!foundChannel.id().equals(channel.getId())) {
            throw new AssertionError(
                    "채널 조회 결과의 ID가 일치하지 않습니다."
            );
        }

        System.out.println("채널 조회 성공");

        return channel;
    }

    private static Message testMessage(
            MessageService messageService,
            Channel channel,
            User user
    ) {
        System.out.println("\n[3] 메시지 생성 및 조회 테스트");

        MessageCreateRequest request =
                new MessageCreateRequest(
                        channel.getId(),
                        user.getId(),
                        "안녕하세요.",
                        Optional.empty()
                );

        Message message =
                messageService.create(request);

        if (message == null || message.getId() == null) {
            throw new AssertionError("메시지 생성에 실패했습니다.");
        }

        System.out.println("메시지 생성 성공");
        System.out.println("메시지 ID: " + message.getId());

        Message foundMessage =
                messageService.find(message.getId());

        if (!foundMessage.getId().equals(message.getId())) {
            throw new AssertionError(
                    "메시지 조회 결과의 ID가 일치하지 않습니다."
            );
        }

        if (!foundMessage.getMessage().equals("안녕하세요.")) {
            throw new AssertionError(
                    "메시지 조회 결과의 내용이 일치하지 않습니다."
            );
        }

        System.out.println("메시지 조회 성공");

        return message;
    }

    private static void testMessageUpdate(
            MessageService messageService,
            Message message
    ) {
        System.out.println("\n[4] 메시지 수정 테스트");

        String updatedContent =
                "수정된 메시지입니다.";

        MessageUpdateRequest request =
                new MessageUpdateRequest(
                        message.getId(),
                        updatedContent
                );

        Message updatedMessage =
                messageService.update(request);

        if (!updatedMessage.getMessage().equals(updatedContent)) {
            throw new AssertionError(
                    "메시지 수정 결과가 일치하지 않습니다."
            );
        }

        Message foundMessage =
                messageService.find(message.getId());

        if (!foundMessage.getMessage().equals(updatedContent)) {
            throw new AssertionError(
                    "수정된 메시지 조회 결과가 일치하지 않습니다."
            );
        }

        System.out.println("메시지 수정 성공");
    }

    private static void testMessageDelete(
            MessageService messageService,
            Message message
    ) {
        System.out.println("\n[5] 메시지 삭제 테스트");

        UUID messageId = message.getId();

        messageService.delete(messageId);

        try {
            messageService.find(messageId);

            throw new AssertionError(
                    "삭제된 메시지가 조회되었습니다."
            );

        } catch (NoSuchElementException e) {
            System.out.println("메시지 삭제 성공");
        }
    }

    private static void testChannelDelete(
            ChannelService channelService,
            Channel channel
    ) {
        System.out.println("\n[6] 채널 삭제 테스트");

        UUID channelId = channel.getId();

        channelService.delete(channelId);

        try {
            channelService.find(channelId);

            throw new AssertionError(
                    "삭제된 채널이 조회되었습니다."
            );

        } catch (NoSuchElementException e) {
            System.out.println("채널 삭제 성공");
        }
    }
}