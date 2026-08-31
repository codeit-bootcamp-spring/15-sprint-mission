package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

public class ServiceFactory {
    UserRepository userRepository;
    ChannelRepository channelRepository;
    MessageRepository messageRepository;

    UserService userService;
    ChannelService channelService;
    MessageService messageService;

    static ServiceFactory singletonServiceFactory;

    private ServiceFactory(boolean isJCF) {
        if (isJCF) {
            this.userRepository = new JCFUserRepository();
            this.channelRepository = new JCFChannelRepository();
            this.messageRepository = new JCFMessageRepository();
        }
        else {
            this.userRepository = new FileUserRepository();
            this.channelRepository = new FileChannelRepository();
            this.messageRepository = new FileMessageRepository();
        }
        this.userService = new BasicUserService(userRepository);
        this.channelService = new BasicChannelService(channelRepository);
        this.messageService = new BasicMessageService(messageRepository, userService, channelService);
    }

    public static ServiceFactory getInstance(boolean isJCF) {
        if (singletonServiceFactory == null) {
            singletonServiceFactory = new ServiceFactory(isJCF);
            return singletonServiceFactory;
        }
        return singletonServiceFactory;
    }

    public UserService getUserService() {
        return userService;
    }

    public ChannelService getChannelService() {
        return channelService;
    }

    public MessageService getMessageService() {
        return messageService;
    }
}
