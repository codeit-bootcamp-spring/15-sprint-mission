package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel {
    private UUID id;
    private String channelName;
    private Long createAt;
    private Long updateAt;
    private List<User> userList;

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

    public List<User> getUserList() {
        return userList;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    public void addUserToChannel(User user){
        this.userList.add(user);
    }

    public void deleteUserToChannel(User user){
        this.userList.remove(user);
    }

    public List<User> getUserInChannel(){
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
