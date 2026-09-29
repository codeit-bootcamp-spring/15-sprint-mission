package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.*;

//누가,머라고,반응,
@Getter
public class Message extends BaseClass  {
    private final UUID channelId;
    private final UUID authorId;
    private String content;
    private List<UUID> attachmentIds;


    //////////////////////////////////



    public Message(UUID channelId, UUID userId , String content, List<UUID> attachmentIds){
        this.channelId=channelId;
        this.authorId =userId;
        this.content = content;
        this.attachmentIds = attachmentIds == null
                ? new ArrayList<>()
                : new ArrayList<>(attachmentIds);
    }





    public void update(String message){
        this.content =message;
        //this.attachmentIds=attachmentIds;
        setUpdatedAt();
    }


}
