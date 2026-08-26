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
    public void create(UUID channelId,UUID userId, String message) {
        Message messageCreate = new Message(userId,message);

        JCFChannelService.getInstance()
                .addMessage(channelId, userId, messageCreate.getId());

        messageMap.put(messageCreate.getId(),messageCreate);
        reactionMap.put(messageCreate.getId(),new HashMap<>());
        testMessage = messageCreate;

    }

    @Override
    public void read() {
        for (Map.Entry<UUID, Message> entry : messageMap.entrySet()) {
            System.out.println("ID: " + entry.getValue().getId());
            System.out.println("메세지: " + entry.getValue().getMessage());
            System.out.println("보낸사람: " + entry.getValue().getUserId());
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
        reactionMap.remove(id);
        JCFChannelService.getInstance()
                .delete_MessageToChannel(id);


    }

    public Map<UUID, Message> getMessageMap() {
        return new HashMap<>(messageMap);
    }

    public void setMessageMap(Map<UUID, Message> messageMap) {
        this.messageMap.clear();
        this.messageMap.putAll(messageMap);
    }


    public Map<UUID, Map<Reaction, Set<UUID>>> getReactionMap() {
        return new HashMap<>(reactionMap);
    }

    public void setReactionMap(Map<UUID, Map<Reaction, Set<UUID>>> reactionMap) {
        this.reactionMap.clear();
        this.reactionMap.putAll(reactionMap);
    }

    ////////////////////////////////////////
    public void toggleReaction(UUID messageId, UUID userId, Reaction reaction) {
        if(!messageMap.containsKey(messageId)){
            throw new IllegalArgumentException("메세지 ID가 없습니다.");
        }

        if(!JCFUserService.getInstance().userMap.containsKey(userId)){
            throw new IllegalArgumentException("유저 ID가 없습니다.");
        }

        reactionMap.get(messageId).computeIfAbsent(reaction, key -> new HashSet<>());
        if(reactionMap.get(messageId).get(reaction).contains(userId)){
            reactionMap.get(messageId).get(reaction).remove(userId);
        }else {
            reactionMap.get(messageId).get(reaction).add(userId);
        }
    }

    public void printReactionCount(UUID messageid){
        if(reactionMap.containsKey(messageid)){
            for (Reaction reaction : reactionMap.get(messageid).keySet()){
                System.out.println(reaction + " : 의 수 " + reactionMap.get(messageid).get(reaction).size());
            }
        } else {
            throw new IllegalArgumentException("해당 id가 없습니다.");
        }

        /*if(reactionMap.containsKey(messageid)){
            if(reactionMap.get(messageid).containsKey(reaction)){
                System.out.println(reactionMap.get(messageid).get(reaction).size());
            }else {
                System.out.println(0);
            }
        }else  {
            throw new IllegalArgumentException("해당 id가 없습니다.");
        }*/

    }

    public void delete_UserToMessage(UUID userId){
        for(Map<Reaction, Set<UUID>> entry : reactionMap.values()){
            for(Reaction reaction : entry.keySet()){
                entry.get(reaction).remove(userId);
            }
        }
    }
///////////////////////////////////
    //
    public void delete_ChannelToMessage(UUID channelId){
        /*if(JCFChannelService.getInstance().userRoleMap.containsKey(channelId)){
            throw  new IllegalArgumentException("채널 id가 없습니다");
        }*/

        //채널에 속한 메세지들을 jcf에서 제거
        for(UUID id : JCFChannelService.getInstance().messagesListMap.get(channelId)){
            messageMap.remove(id);
            reactionMap.remove(id);
        }
    }
////////////////////////////////////



    //////////////////////////////////
    public Message getTestMessage() {
        return testMessage;
    }
}
