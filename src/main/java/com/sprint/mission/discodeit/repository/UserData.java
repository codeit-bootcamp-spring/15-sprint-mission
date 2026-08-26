package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserData {
    /*
    final Map<UUID, User> userMap = new HashMap<>();
     */
    private Map<UUID, User> userMap;

    public UserData() { }

    public UserData(Map<UUID, User> userMap) {
        this.userMap = userMap;
    }

    public Map<UUID, User> getUserMap() {
        return new HashMap<>(userMap);
    }

    public void setUserMap(Map<UUID, User> userMap) {
        this.userMap = userMap;
    }
}
