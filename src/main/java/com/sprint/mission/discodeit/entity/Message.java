package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.*;

@Getter
@Entity
@Table(name = "messages")
public class Message extends BaseUpdatableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id",nullable = false)
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @Column(columnDefinition = "TEXT")
    private String content;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "message_attachments",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "attachment_id")
    )
    private List<BinaryContent> attachments =  new ArrayList<>();






    protected Message(){

    }


    public Message(Channel channel, User author , String content, List<BinaryContent> attachments){
        this.channel=channel;
        this.author =author;
        this.content = content;
        this.attachments = attachments == null
                ? new ArrayList<>()
                : new ArrayList<>(attachments);
    }





    public void update(String message){
        this.content =message;
    }


}
