package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelRole;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.*;

//메세지 리스트
//(멤버,권한) 맵
public class JCFChannelService implements ChannelService{

    final Map<UUID, Map<UUID, ChannelRole>> userRoleMap = new HashMap<>();//key channel, value-value user
    final Map<UUID, List<UUID>>  messagesListMap = new HashMap<>();//key channel, value message
    final Set<Channel>  channelSet = new HashSet<>();
    //final Map<UUID, >

    ////////////////////테스트용 나중에 삭제
    public Map<UUID, List<UUID>> getMessagesListMap() {
        return messagesListMap;
    }

    ////////////////////////////
    Channel testChannel;

    private final static JCFChannelService instance = new JCFChannelService();

    private JCFChannelService() { }

    public static JCFChannelService getInstance() {
        return instance;
    }






    @Override
    public void create(String name) {
        for (Channel channel : channelSet) {
            if (channel.getName().equals(name)) {
                throw new IllegalArgumentException("이미 존재하는 채널명");
            }
        }

        Channel channelCreate = new Channel(name);
        userRoleMap.put(channelCreate.getId(),new HashMap<>());
        messagesListMap.put(channelCreate.getId(),new ArrayList<>());
        channelSet.add(channelCreate);
        testChannel = channelCreate;
    }

    @Override
    public void read() {
        for(Channel channel : channelSet){
            System.out.println("ID: " + channel.getId());
            System.out.println("채널명: " + channel.getName());
            System.out.println("수정시간: " + channel.getUpdatedAt());
        }


    }

    @Override
    public void update(UUID id, String name) {
        boolean cheekID=false;
        for(Channel channel : channelSet){
            if(channel.getId().equals(id)){
                channel.update(name);
                cheekID=true;
            }
        }
        if(!cheekID){
            throw new IllegalArgumentException("해당 id가 없습니다.");
        }

    }

    @Override
    public void delete(UUID id) {

        JCFMessageService.getInstance().delete_ChannelToMessage(id);

        userRoleMap.remove(id);
        messagesListMap.remove(id);
        channelSet.removeIf(channel -> channel.getId().equals(id));



    }

    /////////////////////////////////////////////////
    public void putUser(UUID channelId, UUID userId, ChannelRole channelRole) {
        if(userRoleMap.containsKey(channelId)){
            userRoleMap.get(channelId).put(userId, channelRole);
        }
    }

    public void removeUser(UUID channelId, UUID userId) {
        if(userRoleMap.containsKey(channelId)){
            userRoleMap.get(channelId).remove(userId);
        }
    }

    public void addMessage(UUID channelId, UUID userId, UUID messageId) {
        if(userRoleMap.containsKey(channelId)){
            if(userRoleMap.get(channelId).containsKey(userId)){
                messagesListMap.get(channelId).add(messageId);
            }
        }
    }

   /* public void removeMessage(UUID channelId, UUID messageId) {
        if(userRoleMap.containsKey(channelId)){
            if(userRoleMap.get(channelId).containsKey(messageId)){
                messagesListMap.get(channelId).remove(messageId);
            }
        }
    }*/


    public void delete_UserToChannel(UUID userId){
        for(Map<UUID, ChannelRole> entry : userRoleMap.values()){
            entry.remove(userId);
        }

    }

    public void delete_MessageToChannel(UUID messageId){
        for(List<UUID> messageList : messagesListMap.values()){
            messageList.remove(messageId);
        }
    }


    ///////////////////////////////////////////
    public Channel getTestChannel() {
        return testChannel;
    }
}
