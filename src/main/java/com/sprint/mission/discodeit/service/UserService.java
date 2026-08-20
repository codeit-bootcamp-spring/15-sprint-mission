package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.entity.User;

import java.util.Set;

public interface UserService {
    void create(User user);
    User read(User user);
    Set<User> readAll();
    void update(User user, String data);
    void delete(User user);
}
