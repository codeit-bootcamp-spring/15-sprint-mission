package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.entity.User;

import java.util.Set;

public interface UserService {
    User create(String name, String email, String id);
    User read(String name);
    Set<User> readAll();
    void update(User user, String data, String email, String id);
    void delete(User user);
}
