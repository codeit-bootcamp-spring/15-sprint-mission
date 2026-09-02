package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private String channelName;
    private final Long createAt;
    private Long updateAt;
    private List<UUID> userList;

    public Channel(String channelName){
        this.id = UUID.randomUUID();
        this.channelName = channelName;
        this.createAt = System.currentTimeMillis();
        this.updateAt = this.createAt;
        this.userList = new ArrayList<>();
    }

    public void update(String nickName){
        this.channelName = nickName;
        this.updateAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public String getChannelName() {
        return channelName;
    }

    public List<UUID> getUserList() {
        return userList;
    }

    public Long getUpdateAt() {
        return updateAt;
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
                "id=" + id +
                ", channelName='" + channelName + '\'' +
                ", createAt=" + Instant.ofEpochMilli(createAt) +
                ", updateAt=" + Instant.ofEpochMilli(updateAt) +
                '}';
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", channelName='" + channelName + '\'' +
                ", createAt=" + Instant.ofEpochMilli(createAt) +
                ", updateAt=" + Instant.ofEpochMilli(updateAt) +
                ", userList=" + userList +
                '}';
    }


}
