package com.sprint.mission.discodeit.entity;

public class Message extends Common {
    private String message;
    private User user;
    private Channel channel;

    public Message(String message, User user, Channel channel) {
        super();
        this.message = message;
        this.user = user;
        this.channel = channel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public Channel getChannel() {
        return channel;
    }

/*    @Override
    public String toString() {
        return "Account{" +
                "id='" + this.getId() + '\'' +
                ", user='" + this.getUser() + '\'' +
                ", channel='" + this.getChannel() + '\'' +
                ", message='" + this.getMessage() + '\'' +
                ", creadtedAt='" + this.getCreatedAt() + '\'' +
                ", updatedAt='" + this.getUpdatedAt() + '\'' +
                '}';
    }*/
}
