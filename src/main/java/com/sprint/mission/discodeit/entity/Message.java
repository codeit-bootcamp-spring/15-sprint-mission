package com.sprint.mission.discodeit.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//누가,머라고,반응,
public class Message extends BaseClass  {
    private final User user;
    private String message;
    private final Map<Reaction, Set<User>> reactionMap = new HashMap<>();

    public Message(User user , String messageString){
        this.user=user;
        this.message=messageString;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
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
