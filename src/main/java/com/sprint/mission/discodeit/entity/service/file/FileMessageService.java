package com.sprint.mission.discodeit.entity.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.service.ChannelService;
import com.sprint.mission.discodeit.entity.service.MessageService;
import com.sprint.mission.discodeit.entity.service.UserService;

import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService, Serializable {

    private final Map<UUID, Message> data;
    private final ChannelService ChannelService;
    private final UserService UserService;

    @Serial
    private static final long serialVersionUID = 1L;

    public FileMessageService(UserService userService, ChannelService channelService) {
        this.ChannelService = channelService;
        this.UserService = userService;
        this.data = new LinkedHashMap<>();
    }


    @Override
    public Message createMessage(String content, User user, Channel channel) {
        User user1 = this.UserService.getById(user.getId());
        Channel channel1 = this.ChannelService.getById(channel.getId());
        if (user1 != null) {
            if (channel1 != null) {
                Message message = new Message(content, user.getId(), channel.getId());
                data.put(message.getId(), message);
                // 직렬화
                try (FileOutputStream fos = new FileOutputStream("message.ser");
                     ObjectOutputStream oos = new ObjectOutputStream(fos);
                ) {
                    oos.writeObject(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        return data.get(id);
    }

    public List<Message> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Message update(UUID id, String content) {
        data.get(id).update(content);
        data.put(id, data.get(id));
        // 직렬화
        try (FileOutputStream fos = new FileOutputStream("message.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data.get(id);
    }

    @Override
    public boolean deleteByID(UUID id) {
        boolean result = data.remove(id, data.get(id));
        try (FileOutputStream fos = new FileOutputStream("message.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
