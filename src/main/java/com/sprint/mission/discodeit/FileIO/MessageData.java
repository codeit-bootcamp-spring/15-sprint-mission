package com.sprint.mission.discodeit.FileIO;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Reaction;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MessageData {
    /*
    final Map<UUID, Message>  messageMap = new HashMap<>();//UUID=message
    final Map<UUID ,Map<Reaction, Set<UUID>>> reactionMap = new HashMap<>();
     */

    private Map<UUID, Message> messageMap;
    private Map<UUID, Map<Reaction, Set<UUID>>> reactionMap;

    public MessageData(Map<UUID, Message> messageMap, Map<UUID, Map<Reaction, Set<UUID>>> reactionMap) {
        this.messageMap = messageMap;
        this.reactionMap = reactionMap;
    }

    public Map<UUID, Message> getMessageMap() {
        return new HashMap<>(messageMap);
    }

    public Map<UUID, Map<Reaction, Set<UUID>>> getReactionMap() {
        return new HashMap<>(reactionMap);
    }
}
