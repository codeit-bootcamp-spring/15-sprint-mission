package com.sprint.mission.discodeit.entity.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.service.ChannelService;
import com.sprint.mission.discodeit.entity.service.MessageService;
import com.sprint.mission.discodeit.entity.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;
    private final ChannelService ChannelService;
    private final UserService UserService;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.ChannelService = channelService;
        this.UserService = userService;
        this.data = new LinkedHashMap<>();
    }



    // 메시지를 생성할 때 필요한 모델의 데이터? 보내는 사람 이름, 전화번호
    @Override
    public Message createMessage(String content, User user, Channel channel) {
        // 객체를 넘겨받아서,
        // 각 객체의 아이디가 Map에 포함돼 있는지 유효성 검증을 해야한다.
//        if (userMap.containsKey(user.getId())) {
//            if (channelMap.containsKey(channel.getId())) {
//                Message message = new Message(content, user.getId(), channel.getId());
//                data.put(message.getId(), message);
//                return message;
//            }
//        }

        // 유저 아이디를 유저 객체에서 가져온다.
        // 그 객체가 null이 아닌지를 판별한다. -> 유효성 검즏
        User user1 = this.UserService.getById(user.getId());
        Channel channel1 = this.ChannelService.getById(channel.getId());
        if (user1 != null) {
            if (channel1 != null) {
                Message message = new Message(content, user.getId(), channel.getId());
                data.put(message.getId(), message);
                return message;
            }
            else {
                throw new IllegalArgumentException("존재하지 않는 유저 아이디입니다. " + user.getId());
            }
        } else {
            throw new IllegalArgumentException("존재하지 않는 채널 아이디입니다. " + user.getId());
        }
    }

    @Override
    public Message getById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Message update(UUID id, String content) {
        data.get(id).update(content);
        data.put(id, data.get(id));
        return data.get(id);
    }

    @Override
    public boolean deleteByID(UUID id) {
        return data.remove(id, data.get(id));
    }

    // data 필드를 활용해 생성, 조회, 수정, 삭제하는 메소드를 구현하세요.
}
