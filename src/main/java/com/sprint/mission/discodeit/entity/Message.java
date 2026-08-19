package com.sprint.mission.discodeit.entity;

import java.util.*;

//누가,머라고,반응,
public class Message extends BaseClass  {
    private final UUID userId;
    private String message;

    //////////////////////////////////
    //private final Map<Reaction, Set<User>> reactionMap = new HashMap<>();
    //메세지에 좋아요, 싫어요를 누를 수 있는 기능
    //////////////////////////////////

    public Message(UUID userId , String message){
        this.userId=userId;
        this.message=message;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }



    public void update(String message){
        this.message=message;
        setUpdatedAt();
    }

    /*public void setMessage(String messageString) {
        this.message = messageString;
    }*/

    /*public void buttonReaction(User user , Reaction reaction) {
        reactionMap.computeIfAbsent(reaction, key -> new HashSet<>());
        if(reactionMap.get(reaction).contains(user)){
            reactionMap.get(reaction).remove(user);
        }else {
            reactionMap.get(reaction).add(user);

        }
    }

    public int getReaction(Reaction reaction){
        if(reactionMap.containsKey(reaction)){
            return reactionMap.get(reaction).size();
        }else {
            return 0;
        }
    }*/




}
