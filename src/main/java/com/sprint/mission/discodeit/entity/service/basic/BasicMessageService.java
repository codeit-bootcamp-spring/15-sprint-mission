package com.sprint.mission.discodeit.entity.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.service.ChannelService;
import com.sprint.mission.discodeit.entity.service.MessageService;
import com.sprint.mission.discodeit.entity.service.UserService;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;

import java.io.Serial;
import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    // private final JCFMessageRepository jcfMessageRepository;
    private final FileMessageRepository fileMessageRepository;
    private final ChannelService ChannelService;
    private final UserService UserService;

    @Serial
    private static final long serialVersionUID = 1L;

    public BasicMessageService(UserService userService, ChannelService channelService) {
        this.ChannelService = channelService;
        this.UserService = userService;
        // this.data = new LinkedHashMap<>(); // 저장 로직
        this.fileMessageRepository = new FileMessageRepository();
    }


    @Override
    public Message createMessage(String content, User user, Channel channel) {
        User user1 = this.UserService.getById(user.getId());
        Channel channel1 = this.ChannelService.getById(channel.getId());
        if (user1 != null) {
            if (channel1 != null) {
                Message message = new Message(content, user.getId(), channel.getId());
                // data.put(message.getId(), message); // 저장 로직
                fileMessageRepository.save(message);
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
        return fileMessageRepository.load(id);
    } // 저장 로직

    public List<Message> readAll() {
        return fileMessageRepository.loadValue();
    } // 저장 로직

    @Override
    public Message update(UUID id, String content) {
        Message message = fileMessageRepository.load(id); // 저장 로직
        message.update(content); // 저장 로직
        // data.put(id, data.get(id));
        fileMessageRepository.save(message);
        // 직렬화

        return message;
    }

    @Override
    public boolean deleteByID(UUID id) {
        boolean result = fileMessageRepository.delete(id); // 저장 로직
        return result;
    }
}
