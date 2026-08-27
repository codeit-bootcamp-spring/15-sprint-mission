package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelRole;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

//메세지 리스트
//(멤버,권한) 맵
public class JCFChannelService implements ChannelService {

    final Map<UUID, Map<UUID, ChannelRole>> userRoleMap = new HashMap<>();//key channel, value-value user
    final Map<UUID, List<UUID>>  messagesListMap = new HashMap<>();//key channel, value message
    final Map<UUID, Channel>  channelMap = new HashMap<>();
    //final Map<UUID, >


    public Map<UUID, List<UUID>> getMessagesListMap() {
        return new HashMap<>(messagesListMap);
    }

    public void setMessagesListMap(Map<UUID, List<UUID>> messagesListMap) {
        this.messagesListMap.clear();
        this.messagesListMap.putAll(messagesListMap);
    }

    public Map<UUID, Map<UUID, ChannelRole>> getUserRoleMap() {
        return new HashMap<>(userRoleMap);
    }

    public void setUserRoleMap(Map<UUID, Map<UUID, ChannelRole>> userRoleMap) {
        this.userRoleMap.clear();
        this.userRoleMap.putAll(userRoleMap);
    }

    public Map<UUID, Channel> getChanelMap() {
        return new HashMap<>(channelMap);
    }

    public void setChanelMap(Map<UUID, Channel> chanelMap) {
        this.channelMap.clear();
        this.channelMap.putAll(chanelMap);
    }

    /*public Set<Channel> getChannelSet() {
        return new HashSet<>(channelSet);
    }

    public void setChannelSet(Set<Channel> channelSet) {
        this.channelSet.clear();
        this.channelSet.addAll(channelSet);
    }*/

    ////////////////////////////
    Channel testChannel;

    private final static JCFChannelService instance = new JCFChannelService();

    private JCFChannelService() { }

    public static JCFChannelService getInstance() {
        return instance;
    }






    @Override
    public void create(String name) {
        for (UUID channelID : channelMap.keySet()) {
            if (channelMap.get(channelID).getName().equals(name)) {
                throw new IllegalArgumentException("이미 존재하는 채널명");
            }
        }

        Channel channelCreate = new Channel(name);
        userRoleMap.put(channelCreate.getId(),new HashMap<>());
        messagesListMap.put(channelCreate.getId(),new ArrayList<>());
        channelMap.put(channelCreate.getId(), channelCreate);
        testChannel = channelCreate;
    }

    @Override
    public void read() {
        for(Channel channel : channelMap.values()){
            System.out.println("ID: " + channel.getId());
            System.out.println("채널명: " + channel.getName());
            System.out.println("생성시간: "+ channel.getCreatedAt());
            System.out.println("수정시간: " + channel.getUpdatedAt());
        }
    }

    @Override
    public void update(UUID id, String name) {
        boolean cheekID=false;

        for (UUID channelID : channelMap.keySet()) {
            if (channelMap.get(channelID).getName().equals(name)) {
                throw new IllegalArgumentException("이미 존재하는 채널명");
            }
        }
        if(!channelMap.containsKey(id)){
            throw new IllegalArgumentException("해당 id가 없습니다.");
        }


        channelMap.get(id).update(name);


    }

    @Override
    public void delete(UUID id) {

        if(userRoleMap.containsKey(id)){
            JCFMessageService.getInstance().delete_ChannelToMessage(id);
        }

        userRoleMap.remove(id);
        messagesListMap.remove(id);
        channelMap.remove(id);
        //channelSet.removeIf(channel -> channel.getId().equals(id));
    }

    /////////////////////////////////////////////////
    public void putUser(UUID channelId, UUID userId, ChannelRole channelRole) {
        if(!userRoleMap.containsKey(channelId)){
            throw new IllegalArgumentException("해당 채널 id가 없습니다.");
        }
        if(!JCFUserService.getInstance().userMap.containsKey(userId)){
            throw new IllegalArgumentException("해당 유저 id가 없습니다.");
        }

        userRoleMap.get(channelId).put(userId, channelRole);
    }
    public void printUsers(UUID channelId) {
        if(!userRoleMap.containsKey(channelId)){
            throw new IllegalArgumentException("해당 채널 id가 없습니다.");
        }
        for(Map.Entry<UUID, ChannelRole> map : userRoleMap.get(channelId).entrySet()){
            System.out.println(map.getKey());
            //System.out.println(JCFUserService.getInstance().getUsername(map.getKey()) + "의 권한 :" + map.getValue());
            System.out.println(JCFUserService.getInstance().getUserMap().get(map.getKey()).getName() + "의 권한 :" + map.getValue());
        }
    }

    public void removeUser(UUID channelId, UUID userId) {
        if(userRoleMap.containsKey(channelId)){
            userRoleMap.get(channelId).remove(userId);
        }
    }

    public void addMessage(UUID channelId, UUID userId, UUID messageId) {
        if(!userRoleMap.containsKey(channelId)){
            throw new IllegalArgumentException("채널 id가 없습니다.");
        }

        if(!userRoleMap.get(channelId).containsKey(userId)){
            throw new IllegalArgumentException("유저 id가 없거나 유저가 채널 소속이 아닙니다.");
        }
        messagesListMap.get(channelId).add(messageId);
        /*if(userRoleMap.containsKey(channelId)){
            if(userRoleMap.get(channelId).containsKey(userId)){
                messagesListMap.get(channelId).add(messageId);
            }
        }*/
    }


    public void printMessageList(UUID channelId){
        if(messagesListMap.containsKey(channelId)){
            for(UUID messageId : messagesListMap.get(channelId)){
                for(User user : JCFUserService.getInstance().getUsers()) {
                    if (user.getId().equals(JCFMessageService.getInstance().getMessageMap().get(messageId).getUserId())) {
                        System.out.print(user.getName() + " : ");
                    }
                }
                System.out.println(JCFMessageService.getInstance().getMessageMap().get(messageId).getMessage());
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
