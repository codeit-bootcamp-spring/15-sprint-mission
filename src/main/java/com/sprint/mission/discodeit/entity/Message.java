package com.sprint.mission.discodeit.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//누가,머라고,반응,
public class Message extends BaseClass  {
    private final User user;
    private String message;

    //////////////////////////////////
    private final Map<Reaction, Set<User>> reactionMap = new HashMap<>();
    //메세지에 좋아요, 싫어요를 누를 수 있는 기능
    //////////////////////////////////

    public Message(User user , String message){
        this.user=user;
        this.message=message;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }



    public void update(String message){
        this.message=message;
        setUpdatedAt();
    }

    public void setMessage(String messageString) {
        this.message = messageString;
    }

    public void buttonReaction(User user , Reaction reaction) {
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
    }




}
