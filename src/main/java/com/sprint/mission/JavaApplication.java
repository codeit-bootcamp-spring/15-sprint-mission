package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ServiceFactory;
import com.sprint.mission.discodeit.service.UserService;

public class JavaApplication {
    static User setupUser(UserService userService, String name, String email, String id) {
        User user = userService.read(name);
        if (user == null) {
            return userService.create(name, email, id);
        }
        else {
            return user;
        }
    }

    static Channel setupChannel(ChannelService channelService, String channelName) {
        Channel channel = channelService.read(channelName);
        if (channel == null) {
            return channelService.create(ChannelType.PUBLIC, channelName, "채널입니다.");
        }
        else {
            return channel;
        }
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author, String mes) {
        if (channel != null && author != null) {
            Message message = messageService.create(mes, channel, author);
            if (message != null) {
                System.out.println("메시지 생성: " + message.getMessage());
            }
            else System.out.println("[messageCreate] 생성에 실패했습니다: 채널, 사용자 데이터 없음");
        }
        else {
            System.out.println("[messageCreate] 생성에 실패했습니다: 잘못된 매개변수 입력.");
        }
    }

    static void runUserCrud(UserService userService) {
        User user = setupUser(userService, "woody", "woody@codeit.com", "woody1234");
        User user1 = setupUser(userService, "박석규", "tjrrb0630@codeit.com", "tjrrb0630");

        userService.readAll();

        User readUser = userService.read("woody");

        userService.update(readUser, "우디", "woody@codeit.com", "woody1234");
        System.out.println(userService.read("우디").getEmail());
        userService.delete(readUser);
        System.out.println();
        userService.readAll().forEach(x-> System.out.println(x.getUser()));
        System.out.println();
    }

    static void runChannelCrud(ChannelService channelService) {
        Channel channel = setupChannel(channelService, "공지");
        Channel channel1 = setupChannel(channelService, "일반");

        channelService.readAll();

        Channel readChannel = channelService.read("일반");

        channelService.update(readChannel, ChannelType.PRIVATE, "공지사항", "공지사항 채널입니다.");
        System.out.println(channelService.read("공지사항").getChannelName());
        System.out.println();
        channelService.delete(readChannel);
        channelService.readAll().forEach(x-> System.out.println(x.getChannelName()));
        System.out.println();
    }

    static void runMessageCrud(MessageService messageService, UserService userService, ChannelService channelService) {
        System.out.println();
        messageService.readAll().forEach(x-> System.out.println(x.getMessage()));
        System.out.println();
        Channel channel = setupChannel(channelService, "공지");

        User user = setupUser(userService, "woody", "woody@codeit.com", "woody1234");
        User user1 = setupUser(userService, "박석규", "tjrrb0630@codeit.com", "tjrrb0630");

        messageCreateTest(messageService, channel, user, "반가워요.");
        messageCreateTest(messageService, channel, user1, "안녕하세요.");

        Message readMessage = messageService.read(channel.getId(), user.getId());

        messageService.update(readMessage, "메시지 업데이트 테스트.");
        System.out.println(messageService.read(channel.getId(), user.getId()).getMessage());
        System.out.println();
        messageService.delete(readMessage);
        messageService.readAll().forEach(x-> System.out.println(x.getMessage()));
        System.out.println();
    }

    static void runAll(ServiceFactory serviceFactory) {
        runUserCrud(serviceFactory.getUserService());
        runChannelCrud(serviceFactory.getChannelService());
        runMessageCrud(serviceFactory.getMessageService(), serviceFactory.getUserService(), serviceFactory.getChannelService());
    }

    public static void main(String[] args) {
        ServiceFactory serviceFactory = ServiceFactory.getInstance(false);

        runAll(serviceFactory);
    }

}
