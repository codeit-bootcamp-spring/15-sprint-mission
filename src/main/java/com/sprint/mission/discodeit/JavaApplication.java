package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.Request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;


import java.util.List;

public class JavaApplication {
    static User setupUser(UserService userService) {
        UserCreateRequest request =
                new UserCreateRequest("woody@codeit.com", "woody1234", "woody", NitroLevel.BASIC, null);
        User user = userService.create(request);
        return user;
    }

    static Channel setupChannel(ChannelService channelService) {
        Channel channel = channelService.create("공지", ChannelType.PUBLIC);
        return channel;
    }

    static void messageCreateTest(MessageService messageService , Channel channel, User user) {
        Message message = messageService.create(channel.getId(), user.getId(),"안녕하세요.");
        System.out.println("메시지 생성: " + message.getId());
    }

    public static void main(String[] args) {



        UserRepository userRepository = new FileUserRepository();
        ChannelRepository channelRepository = new FileChannelRepository();
        MessageRepository messageRepository = new FileMessageRepository();


        UserService userService = new BasicUserService(userRepository);
        ChannelService channelService = new BasicChannelService(channelRepository);
        MessageService messageService = new BasicMessageService(messageRepository, channelRepository, userRepository);


        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);
        // 테스트
        messageCreateTest(messageService, channel, user);
        Message testMessage1 = messageService.create(channel.getId(),user.getId(),"실험용1");
        List<Message> messageList = messageService.readAll();
        Message testMessage2 = messageService.read(testMessage1.getId());

        System.out.println("user     : " + testMessage1.getId());
        System.out.println("testuser : " + testMessage2.getId());
        System.out.println("같음? : " + testMessage1.getId().equals(testMessage2.getId()));

        messageService.update(testMessage1.getId(),"WER@asdf.sd");
        messageList = messageService.readAll();
        messageService.delete(testMessage2.getId());
        messageList = messageService.readAll();

        System.out.println("끝");



    }


}
