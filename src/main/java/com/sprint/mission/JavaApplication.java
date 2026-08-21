package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.UUID;

public class JavaApplication {

    public static void main(String[] args) {
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService(userService, channelService);

        User user = userService.create("codeit");
        Channel channel = channelService.create("general");
        Message message = messageService.create(
                user.getId(),
                channel.getChannelId(),
                "안녕하세요."
        );

        // 단건 조회
        System.out.println("User 조회: " + userService.read(user.getId()).getUsername());
        System.out.println("Channel 조회: " + channelService.read(channel.getChannelId()).getChannelName());
        System.out.println("Message 조회: " + messageService.read(message.getMessageId()).getContent());

        // 다건 조회
        System.out.println("전체 User 수: " + userService.readAll().size());
        System.out.println("전체 Channel 수: " + channelService.readAll().size());
        System.out.println("전체 Message 수: " + messageService.readAll().size());

        // 수정
        userService.update(user.getId(), "codeit123");
        channelService.update(channel.getChannelId(), "공지사항");
        messageService.update(message.getMessageId(), "수정된 메시지입니다.");

        // 수정된 데이터 조회
        System.out.println("수정된 User: " + userService.read(user.getId()).getUsername());
        System.out.println("수정된 Channel: " + channelService.read(channel.getChannelId()).getChannelName());
        System.out.println("수정된 Message: " + messageService.read(message.getMessageId()).getContent());

        // 존재하지 않는 User로 Message를 생성할 수 없는지 확인
        try {
            messageService.create(UUID.randomUUID(), channel.getChannelId(), "생성할 수 없는 메시지");
        } catch (IllegalArgumentException exception) {
            System.out.println("연관 데이터 검증 성공: " + exception.getMessage());
        }

        // 삭제
        messageService.delete(message.getMessageId());
        channelService.delete(channel.getChannelId());
        userService.delete(user.getId());

        // 삭제 확인
        System.out.println("Message 삭제 확인: " + (messageService.read(message.getMessageId()) == null));
        System.out.println("Channel 삭제 확인: " + (channelService.read(channel.getChannelId()) == null));
        System.out.println("User 삭제 확인: " + (userService.read(user.getId()) == null));
    }
}
