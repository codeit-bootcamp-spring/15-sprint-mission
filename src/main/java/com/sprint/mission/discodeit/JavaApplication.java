package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.io.IOException;

public class JavaApplication {

    public static void main(String[] args) throws IOException {

        // 서비스 생성
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService(userService, channelService);

        // User 테스트
        User user = new User("길춘배");
        userService.create(user);

        System.out.println("User 등록");
        System.out.println(user.getName());

        // 단건 조회
        User foundUser = userService.findById(user.getId()).orElseThrow();
        System.out.println("단건 조회: " + foundUser.getId());

        // User 전체 조회
        System.out.println("User 전체 조회");
        for (User u : userService.findAll()) {
            System.out.println(u.getName());
        }

        // 삭제 테스트
        System.out.println("삭제할 User: " + foundUser.getName());
        userService.delete(foundUser.getId());

        System.out.println("삭제 후 User 전체 조회");
        for (User u : userService.findAll()) {
            System.out.println(u.getName());
        }

        // Channel 생성
        Channel channel = new Channel("길춘배 채널", ChannelType.PUBLIC);
        channelService.create(channel);

        System.out.println("Channel 전체 조회");
        for (Channel c : channelService.findAll()) {
            System.out.println(c.getName());
        }

        // Message 생성
        Message message = messageService.create(
                "안녕하세요 저는 봄의요정 길춘배",
                channel.getId(),
                user.getId()
        );

        // Message 조회
        Message found = messageService.findById(message.getId()).orElseThrow();
        System.out.println("조회: " + found.getContent());

        System.out.println("전체 메시지 수: " + messageService.findAll().size());

        // 좋아요 테스트
        messageService.like(message.getId(), user.getId());

        System.out.println(
                "좋아요 개수: " +
                        messageService.getLikeUserIds(message.getId()).size()
        );

        try {
            messageService.create("삭제된 유저 테스트", channel.getId(),foundUser.getId());
        }catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

}