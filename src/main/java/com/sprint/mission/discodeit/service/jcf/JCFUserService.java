package com.sprint.mission.discodeit.service.jcf;


import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;


import java.util.*;

public class JCFUserService implements UserService {

    Map<UUID, User> userMap = new HashMap<>();

    @Override
    public User getUser(UUID id) {
        return userMap.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User createUser(String userName) {
        User user = new User(userName);
        userMap.put(user.getId(), user);
        return user;

    }

    @Override
    public User updateUser(UUID id, String userName) {
        User user = userMap.get(id);
        user.updateName(userName);
        return user;
    }

    @Override
    public User deleteUser(UUID id) {
        return userMap.remove(id);
    }


}
