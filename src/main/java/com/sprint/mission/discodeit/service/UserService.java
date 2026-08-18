package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserService {

    void createUser(UUID id,String nickname);

    User getUser(UUID id);

    String getUserNickname(UUID uuid);

    void updateUser(UUID id,String nickname);

    List<User> getUserAll();

    void userDelete(UUID id);
}
