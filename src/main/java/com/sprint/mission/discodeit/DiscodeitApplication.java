package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {

    static User setupUser(UserService userService) {
        System.out.println("=== User CRUD ===");

        User user1 = userService.create("민영", "minyeong@naver.com", "1234a");
        User user2 = userService.create("탱이", "taeng@naver.com", "5678a");
        System.out.println("등록: " + user1.getUserName() + ", " + user2.getUserName());

        User foundUser = userService.read(user1.getId());
        System.out.println("단건 조회: " + foundUser.getUserName());
        System.out.println("다건 조회:");
        for (User user : userService.readAll()) {
            System.out.println("- " + user.getUserName());
        }

        userService.update(user1.getId(), "민영(수정됨)", null, null);
        System.out.println("수정 완료");
        System.out.println("수정된 데이터 조회: " + userService.read(user1.getId()).getUserName());
        System.out.println("이메일 확인: " + userService.read(user1.getId()).getEmail());

        userService.delete(user2.getId());
        System.out.println("삭제 완료");
        System.out.println("삭제 후 조회:");
        for (User user : userService.readAll()) {
            System.out.println("- " + user.getUserName());
        }

        return user1;
    }

    static Channel setupChannel(ChannelService channelService) {
        System.out.println("=== Channel CRUD ===");

        Channel channel1 = channelService.create("공부");
        Channel channel2 = channelService.create("수다");
        Channel channel3 = channelService.create("여행");
        System.out.println("등록: " + channel1.getChannelName() + ", " + channel2.getChannelName() + ", " + channel3.getChannelName());
        System.out.println("단건 조회: " + channelService.read(channel1.getId()).getChannelName());
        System.out.println("다건 조회:");
        for (Channel channel : channelService.readAll()) {
            System.out.println("- " + channel.getChannelName());
        }

        channelService.update(channel1.getId(), "공부(수정됨)");
        System.out.println("수정 완료");
        System.out.println("수정된 데이터 조회: " + channelService.read(channel1.getId()).getChannelName());

        channelService.delete(channel2.getId());
        System.out.println("삭제 완료");
        System.out.println("삭제 후 조회:");
        for (Channel channel : channelService.readAll()) {
            System.out.println("- " + channel.getChannelName());
        }

        return channel1;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        System.out.println("=== Message CRUD ===");

        Message message1 = messageService.create("안녕하세요", author.getId(), channel.getId());
        Message message2 = messageService.create("환영합니다", author.getId(), channel.getId());
        System.out.println("등록: " + message1.getContents() + ", " + message2.getContents());
        System.out.println("단건 조회: " + messageService.read(message1.getId()).getContents());
        System.out.println("다건 조회:");
        for (Message message : messageService.readAll()) {
            System.out.println("- " + message.getContents());
        }

        messageService.update(message1.getId(), "안녕하세요(수정됨)");
        System.out.println("수정 완료");
        System.out.println("수정된 데이터 조회: " + messageService.read(message1.getId()).getContents());

        messageService.delete(message1.getId());
        System.out.println("삭제 완료");
        System.out.println("삭제 후 조회:");
        for (Message message : messageService.readAll()) {
            System.out.println("- " + message.getContents());
        }
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        // 서비스 초기화 (Spring Context에서 Bean 조회)
        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        // 셋업
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);
        // 테스트
        messageCreateTest(messageService, channel, user);
    }
}