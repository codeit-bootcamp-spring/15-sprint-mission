package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Reaction;
import com.sprint.mission.discodeit.entity.User;

import java.util.*;

//반응 숫자 맵
public class JCFMessageService implements MessageService{
    final Map<UUID, Message>  messageMap = new HashMap<>();//UUID=message
    final Map<UUID ,Map<Reaction, Set<UUID>>> reactionMap = new HashMap<>();//key 메세지, value-value 유저
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
        reactionMap.put(messageCreate.getId(),new HashMap<>());
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
    ////////////////////////////////////////
    public void toggleReaction(UUID messageid, UUID userid, Reaction reaction) {
        reactionMap.get(messageid).computeIfAbsent(reaction, key -> new HashSet<>());
        if(reactionMap.get(messageid).get(reaction).contains(userid)){
            reactionMap.get(messageid).get(reaction).remove(userid);
        }else {
            reactionMap.get(messageid).get(reaction).add(userid);
        }
    }

    public int getReactionCount(UUID messageid,Reaction reaction){
        if(reactionMap.containsKey(messageid)){
            if(reactionMap.get(messageid).containsKey(reaction)){
                return reactionMap.get(messageid).get(reaction).size();
            }else {
                return 0;
            }
        }else  {
            throw new IllegalArgumentException("해당 id가 없습니다.");
        }

    }

    /*
    public void buttonReaction(User user , Reaction reaction) {
        reactionMap.computeIfAbsent(reaction, key -> new HashSet<>());
        if(reactionMap.get(reaction).contains(user)){
            reactionMap.get(reaction).remove(user);
        }else {
            reactionMap.get(reaction).add(user);

        }
    }

     */

    //////////////////////////////////
    public Message getTestMessage() {
        return testMessage;
    }
}
