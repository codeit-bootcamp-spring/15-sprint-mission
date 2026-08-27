package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.NitroLevel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;


import java.util.List;
import java.util.UUID;

public class JavaApplication {
    static User setupUser() {
        User user = BasicUserService.getInstance().create("woody@codeit.com", "woody1234", "woody", NitroLevel.BASIC);
        return user;
    }

    static Channel setupChannel() {
        Channel channel = BasicChannelService.getInstance().create("공지");
        return channel;
    }

    static void messageCreateTest(Channel channel, User user) {
        Message message = BasicMessageService.getInstance().create(channel.getId(), user.getId(),"안녕하세요.");
        System.out.println("메시지 생성: " + message.getId());
    }

    public static void main(String[] args) {



        BasicUserService.getInstance().setUserRepository(FileUserRepository.getInstance());
        BasicChannelService.getInstance().setChannelRepository(FileChannelRepository.getInstance());
        BasicMessageService.getInstance().setMessageRepository(FileMessageRepository.getInstance());

        /*BasicUserService.getInstance().setUserRepository(JCFUserRepository.getInstance());
        BasicChannelService.getInstance().setChannelRepository(JCFChannelRepository.getInstance());
        BasicMessageService.getInstance().setMessageRepository(JCFMessageRepository.getInstance());*/

        User user = setupUser();
        Channel channel = setupChannel();
        // 테스트
        messageCreateTest(channel, user);

        List<User> userList = BasicUserService.getInstance().readAll();
        User testuser = BasicUserService.getInstance().read(user.getId());

        System.out.println("user     : " + user.getId());
        System.out.println("testuser : " + testuser.getId());
        System.out.println("같음? : " + user.getId().equals(testuser.getId()));

        BasicUserService.getInstance().update(user.getId(),"WER@asdf.sd","asdf","asdf",NitroLevel.CLASSIC);
        userList = BasicUserService.getInstance().readAll();
        BasicUserService.getInstance().delete(user.getId());
        userList = BasicUserService.getInstance().readAll();

        System.out.println("끝");



    }


}
