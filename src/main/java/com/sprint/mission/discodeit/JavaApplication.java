package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.NitroLevel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;


import java.util.List;

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
        Message testMessage1 = BasicMessageService.getInstance().create(channel.getId(),user.getId(),"실험용1");
        List<Message> messageList = BasicMessageService.getInstance().readAll();
        Message testMessage2 = BasicMessageService.getInstance().read(testMessage1.getId());

        System.out.println("user     : " + testMessage1.getId());
        System.out.println("testuser : " + testMessage2.getId());
        System.out.println("같음? : " + testMessage1.getId().equals(testMessage2.getId()));

        BasicMessageService.getInstance().update(testMessage1.getId(),"WER@asdf.sd");
        messageList = BasicMessageService.getInstance().readAll();
        BasicMessageService.getInstance().delete(testMessage2.getId());
        messageList = BasicMessageService.getInstance().readAll();

        System.out.println("끝");



    }


}
