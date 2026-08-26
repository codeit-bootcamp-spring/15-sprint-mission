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
    private Map<UUID, Channel> channelMap;

    public ChannelData() { }

    public ChannelData(Map<UUID, Map<UUID, ChannelRole>> userRoleMap, Map<UUID, List<UUID>>  messagesListMap, Map<UUID, Channel> channelMap) {
        this.userRoleMap = userRoleMap;
        this.messagesListMap = messagesListMap;
        this.channelMap = channelMap;
    }

    public Map<UUID, Map<UUID, ChannelRole>> getUserRoleMap() {
        return new HashMap<>(userRoleMap);
    }

    public Map<UUID, List<UUID>> getMessagesListMap() {
        return new HashMap<>(messagesListMap);
    }

    public Map<UUID, Channel> getChannelMap() {
        return new HashMap<>(channelMap);
    }

    public void setChannelMap(Map<UUID, Channel> channelMap) {
        this.channelMap = channelMap;
    }

    /*public Set<Channel> getChannelSet() {
        return new HashSet<>(channelSet);
    }

    public void setChannelSet(Set<Channel> channelSet) {
        this.channelSet = channelSet;
    }*/

    public void setMessagesListMap(Map<UUID, List<UUID>> messagesListMap) {
        this.messagesListMap = messagesListMap;
    }

    public void setUserRoleMap(Map<UUID, Map<UUID, ChannelRole>> userRoleMap) {
        this.userRoleMap = userRoleMap;
    }
}
