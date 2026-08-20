package com.sprint.mission.discodeit.entity.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.service.file.FileChannelService;
import com.sprint.mission.discodeit.entity.service.file.FileMessageService;
import com.sprint.mission.discodeit.entity.service.file.FileUserService;
import com.sprint.mission.discodeit.entity.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.entity.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.entity.service.jcf.JCFUserService;

public class ServiceFactory {

    // 싱글톤 적용. Service
    private static final ServiceFactory INSTANCE = new ServiceFactory();

    // 서비스 객체들을 하나만 유지하기 위해 final인 필드를 만든다.
    private final UserService userService;
    private final ChannelService channelService;
    private final MessageService messageService;

    public ServiceFactory() { // 생성자로 의존성 주입
        this.userService = new FileUserService();
        this.channelService = new FileChannelService();
        this.messageService = new FileMessageService(this.userService, this.channelService);
    }
    // -> getter setter

    public ServiceFactory getInstance() { return INSTANCE; }
    public UserService getUserService() { // 현재 생성된 객체를 얻어와야 하므로
        return this.userService;
    }

    public ChannelService getChannelService() {
        return this.channelService;
    }

    public MessageService getMessageService() {
        return this.messageService;
    }






}
