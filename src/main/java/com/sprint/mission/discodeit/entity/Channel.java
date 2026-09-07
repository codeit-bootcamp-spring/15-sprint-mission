package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Channel extends Common implements Serializable {

    private static final long serialVersionUID = 1L;

    private String channelName;
    private Instant updateAt;
    private List<UUID> userList;

    public Channel(String channelName){
        super(UUID.randomUUID());
        this.channelName = channelName;
        this.updateAt = super.getCreateAt();
        this.userList = new ArrayList<>();
    }

    public void update(String nickName){
        this.channelName = nickName;
        this.updateAt = Instant.now();
    }

    public void addUserToChannel(UUID user){
        this.userList.add(user);
    }

    public void deleteUserToChannel(UUID user){
        this.userList.remove(user);
    }

    public List<UUID> getUserInChannel(){
        return this.userList;
    }

    public String getChannelInfo(){
        return "Channel{" +
                "id=" + getId() +
                ", channelName='" + channelName + '\'' +
                ", createAt=" + getCreateAt().atZone(ZoneId.of("Asia/Seoul")) +
                ", updateAt=" + updateAt.atZone(ZoneId.of("Asia/Seoul")) +
                '}';
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + getId() +
                ", channelName='" + channelName + '\'' +
                ", createAt=" + getCreateAt().atZone(ZoneId.of("Asia/Seoul")) +
                ", updateAt=" + updateAt.atZone(ZoneId.of("Asia/Seoul")) +
                ", userList=" + userList +
                '}';
    }


}
