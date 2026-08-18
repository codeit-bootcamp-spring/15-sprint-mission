package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Reaction;
import com.sprint.mission.discodeit.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

//반응 숫자 맵
public class JCFMessageService implements MessageService{
    final Map<UUID, Message>  messageMap = new HashMap<>();//UUID=message
    final Map<UUID ,Map<Reaction, Set<User>>> reactionMap = new HashMap<>();//UUID=message
    //2nf?
    Message testMessage;

    private final static JCFMessageService instance = new JCFMessageService();

    private JCFMessageService() { }

    public static JCFMessageService getInstance() {
        return instance;
    }


    @Override
    public void create(User user, String message) {
        Message messageCreate = new Message(user,message);
        messageMap.put(messageCreate.getId(),messageCreate);
        testMessage = messageCreate;
    }

    @Override
    public void read() {
        for (Map.Entry<UUID, Message> entry : messageMap.entrySet()) {
            System.out.println("ID: " + entry.getValue().getId());
            System.out.println("메세지: " + entry.getValue().getMessage());
            System.out.println("보낸사람: " + entry.getValue().getUser().getName());
            System.out.println("수정시간: " + entry.getValue().getUpdatedAt());
        }

    }

    @Override
    public void update(UUID id , String message) {
        if(!messageMap.containsKey(id)){
            throw new IllegalArgumentException("해당 id가 없습니다.");
        }
        messageMap.get(id).update( message);

    }

    @Override
    public void delete(UUID id) {
        messageMap.remove(id);

    }

    /////////////////////////
    public Message getTestMessage() {
        return testMessage;
    }
}
