package com.sprint.mission.discodeit.entity;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
