package com.sprint.mission.discodeit.entity.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.service.ChannelService;
import com.sprint.mission.discodeit.entity.service.MessageService;
import com.sprint.mission.discodeit.entity.service.UserService;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;

import java.util.*;

public class JCFMessageService implements MessageService {
    // private final Map<UUID, Message> data; // 저장 로직
    private final ChannelService ChannelService;
    private final UserService UserService;
    private final JCFMessageRepository jcfMessageRepository;


    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.ChannelService = channelService;
        this.UserService = userService;
        // this.data = new LinkedHashMap<>(); // 저장 로직
        this.jcfMessageRepository = new JCFMessageRepository();
    }



    // 메시지를 생성할 때 필요한 모델의 데이터? 보내는 사람 이름, 전화번호
    @Override
    public Message createMessage(String content, User user, Channel channel) {
        User user1 = this.UserService.getById(user.getId()); // 비즈니스 로직
        Channel channel1 = this.ChannelService.getById(channel.getId());
        if (user1 != null) {
            if (channel1 != null) {
                Message message = new Message(content, user.getId(), channel.getId());
                jcfMessageRepository.save(message);
                return message;
            }
            else {
                throw new IllegalArgumentException("존재하지 않는 채널 아이디입니다. " + channel.getId());
            }
        } else {
            throw new IllegalArgumentException("존재하지 않는 유저 아이디입니다. " + user.getId());
        }
    }

    @Override
    public Message getById(UUID id) {
        return jcfMessageRepository.load(id);
    } // 저장 로직

    @Override
    public List<Message> readAll() {
        return jcfMessageRepository.loadValue();
    } // 저장 로직

    @Override
    public Message update(UUID id, String content) {
        Message message = jcfMessageRepository.load(id); // 저장 로직
        message.update(content);
        jcfMessageRepository.save(message);
        return message;
    }

    @Override
    public boolean deleteByID(UUID id) {
        return jcfMessageRepository.delete(id); // 저장 로직
    }

    // data 필드를 활용해 생성, 조회, 수정, 삭제하는 메소드를 구현하세요.
}
