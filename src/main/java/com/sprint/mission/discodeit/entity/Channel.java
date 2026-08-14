package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//멤버(권한), 메세지
public class Channel {
    private final Map<User, ChannelRole> userMap = new HashMap<>();
    private final List<Message> messages = new ArrayList<>();

    public Channel(){
        super();
    }

    public void setRole(User user, ChannelRole role) {
        userMap.put(user, role);
    }

    public void removeUser(User user) {
        userMap.remove(user);
    }

    public ChannelRole getRole(User user) {
        return userMap.get(user);
    }

    public void addMessage(Message message) {
        if(userMap.containsKey(message.getUser())){
            messages.add(message);
        }
    }



}
