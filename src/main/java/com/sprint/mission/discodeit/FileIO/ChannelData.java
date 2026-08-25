package com.sprint.mission.discodeit.FileIO;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelRole;

import java.util.*;

public class ChannelData {
    /*
    final Map<UUID, Map<UUID, ChannelRole>> userRoleMap = new HashMap<>();//key channel, value-value user
    final Map<UUID, List<UUID>>  messagesListMap = new HashMap<>();//key channel, value message
    final Set<Channel>  channelSet = new HashSet<>();
     */

    private Map<UUID, Map<UUID, ChannelRole>> userRoleMap;
    private Map<UUID, List<UUID>>  messagesListMap;
    private Set<Channel> channelSet;

    public ChannelData(Map<UUID, Map<UUID, ChannelRole>> userRoleMap, Map<UUID, List<UUID>>  messagesListMap, Set<Channel> channelSet) {
        this.userRoleMap = userRoleMap;
        this.messagesListMap = messagesListMap;
        this.channelSet = channelSet;
    }

    public Map<UUID, Map<UUID, ChannelRole>> getUserRoleMap() {
        return new HashMap<>(userRoleMap);
    }

    public Map<UUID, List<UUID>> getMessagesListMap() {
        return new HashMap<>(messagesListMap);
    }

    public Set<Channel> getChannelSet() {
        return new HashSet<>(channelSet);
    }
}
