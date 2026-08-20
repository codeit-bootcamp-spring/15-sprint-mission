package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class JCFMessageService implements MessageService {
    private final ArrayList<Message> data;
    private UserService userService;
    private ChannelService channelService;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
        this.data = new ArrayList<>();
    }


    @Override
    public void create(Message message) {
        try {
            if (channelService.read(message.getChannel()).getId().equals(message.getChannel().getId()) &&
                    userService.read(message.getUser()).getId().equals(message.getUser().getId())) {
                this.data.add(message);
            }
        } catch (NullPointerException e) {
            System.out.println("[메시지 생성 도중 오류 발생]");
            System.out.println("[" + message.getChannel().getChannel() + "]채널이 없거나, 유저(" + message.getUser().getUser() + ")가 존재하지 않습니다.");
            System.out.println();
        }
    }

    @Override
    public Message read (Message message) {
        for (Message m : this.data) {
            if (m.getId().equals(message.getId())) {
                return m;
            }
        }
        return null;
    }

    @Override
    public List<Message> readAll() {
       return this.data;
    }

    @Override
    public void update(Message message, String data) {
        for (Message m : this.data) {
            if (m.getId().equals(message.getId())) {
                m.setMessage(data);
                m.setUpdatedAt();
            }
        }
        System.out.println("변경 완료");
    }

    @Override
    public void delete(Message message) {
        this.data.remove(message);
    }


}
