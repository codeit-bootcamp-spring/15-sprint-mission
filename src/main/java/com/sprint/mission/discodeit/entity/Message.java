package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Message extends Common {
    private String message;
    private Channel channel;
    private User user;

    public Message(String message, Channel channel, User user) {
        super();
        this.message = message;
        this.channel = channel;
        this.user = user;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
