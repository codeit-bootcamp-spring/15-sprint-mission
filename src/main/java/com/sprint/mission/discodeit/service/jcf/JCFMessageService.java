package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data = new HashMap<>();
    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(UserService userService, ChannelService channelService){
        this.userService =userService;
        this.channelService = channelService;
    }
    @Override
    public Message create(UUID userId, UUID channelID, String Message){
        User user = userService.read(userId);
        Channel channel = channelService.read(channelID);
        if (user ==null || channel == null){
            throw new IllegalArgumentException("user나 channel 이 존재하지 않습니다");
        }
        Message message = new Message(Message,channelID,userId);
        data.put(message.getMessageId(), message);
        return message;
    }
    @Override
    public Message read(UUID id){
        return data.get(id);
    }
    @Override
    public List<Message> readAll(){
        return new ArrayList<>(data.values());
    }
    @Override
    public Message update(UUID id, String Message){
        Message contents = data.get(id);
        if (contents != null){
            contents.update(Message);
        }
        return contents;
    }
    @Override
    public Message delete(UUID id){
        return data.remove(id);
    }
}