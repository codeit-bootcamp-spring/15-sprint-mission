package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelRole;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.*;

//메세지 리스트
//(멤버,권한) 맵
public class JCFChannelService implements ChannelService{

    final Map<UUID, Map<User, ChannelRole>> userRoleMap = new HashMap<>();
    final Map<UUID, List<Message>>  messagesListMap = new HashMap<>();
    final Set<Channel>  channelSet = new HashSet<>();
    //final Map<UUID, >


    Channel testChannel;

    private final static JCFChannelService instance = new JCFChannelService();

    private JCFChannelService() { }

    public static JCFChannelService getInstance() {
        return instance;
    }




    @Override
    public void create(String name) {
        if(channelSet.contains(name)){
            throw new IllegalArgumentException("이미 존재하는 채널명");
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
        for(Channel channel : channelSet){
            if(channel.getId().equals(id)){
                channel.update(name);
            }
        }

    }

    @Override
    public void delete(UUID id) {
        userRoleMap.remove(id);
        messagesListMap.remove(id);
        for(Channel channel : channelSet){
            if (channel.getId().equals(id)) {
                channelSet.remove(channel);
            }
        }

    }


    ///////////////////////////////////////////
    public Channel getTestChannel() {
        return testChannel;
    }
}
