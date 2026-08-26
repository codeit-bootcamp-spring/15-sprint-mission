package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

//멤버(권한), 메세지
public class Channel extends BaseClass{
    ///////////////////////////////////////////
    /*private final Map<User, ChannelRole> userMap = new HashMap<>();
    //채널에 속한 멤버들과 멤버의 권한(관리자,일반 등등)
    private final List<Message> messages = new ArrayList<>();*/
    //채널에 대화한 메세지를 list로 기록
    private String name;
    ///////////////////////////////////////////

    @JsonCreator
    public Channel(
            @JsonProperty("id") UUID id,
            @JsonProperty("createdAt") Long createdAt,
            @JsonProperty("updatedAt") Long updatedAt,
            @JsonProperty("name") String name
    ) {
        super(id, createdAt, updatedAt);
        this.name = name;
    }

    public Channel(String name) {
        super();
        this.name = name;
    }

    public void update(String name){
        this.name = name;
        setUpdatedAt();
    }

    public String getName() {
        return name;
    }



    /*public void setName(String name) {
        this.name = name;
    }

    public void setRole(User user, ChannelRole role) {
        userMap.put(user, role);
    }

    public void removeUser(User user) {
        userMap.remove(user);
    }*/

    /*public ChannelRole getRole(User user) {
        return userMap.get(user);
    }

    public void addMessage(Message message) {
        if(userMap.containsKey(message.getUserid())){
            messages.add(message);
        }
    }*/



}
