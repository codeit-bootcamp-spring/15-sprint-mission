package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class JavaApplication {
    public static <user> void main(String[] args) {

        // 서비스 생성
       UserService userService = new JCFUserService();
       ChannelService channelService = new JCFChannelService();
       MessageService messageService = new JCFMessageService(userService,channelService);


        // user 테스트
        User user = new User("길춘배");

        //등록
        userService.create(user);

        System.out.println("User 등록");
        System.out.println(user.getName());

        //단건 조회
        User findUser = userService.findById(user.getId());
        System.out.println("단건 조회"  + findUser.getId());
        System.out.println("삭제할 User" + findUser.getName());
        userService.delete(findUser.getId());

        System.out.println("삭제 후 User 전체 조회");
        for (User u : userService.findAll()) {
            System.out.println(u.getName);
        }


        // user 전체 조회

        System.out.println("User 전체조회");
        for (User u : userService.findAll()) {
            System.out.println(u.getName());
        }
        Channel channel = new Channel("길춘배 채널", ChannelType.PUBLIC);
        channelService.create(channel);
        System.out.println(channel.getName());
        // 채널 조회
        System.out.println("Channel 전체 조회");
        for (Channel c : channelService.findAll()) {
            System.out.println(c.getName());
        }
        // Message  생성
        Message message = new Message(user.getId(), channel.getId(), "안녕하세요 저는 봄의요정 길춘배");
        messageService.create(message);
        System.out.println("Message 등록" + message.getContent());

        Message found = messageService.findById(message.getId());
        System.out.println("조회" + found.getContent());

        System.out.println("전체 메시지 수" + messageService.findAll().size());












    }
}
