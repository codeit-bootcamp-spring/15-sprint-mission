package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFUserService implements UserService {

    public final Map<UUID,User> data;

    public JCFUserService() {
        this.data = new HashMap<>();
    }

    @Override
    public User createUser(String nickname) {
        User user = new User(nickname);
        data.put(user.getId(),user);
        return user;
    }

    @Override
    public User getUser(UUID id) {
        return findUser(id);
    }

    @Override
    public String getUserNickname(UUID uuid) {
        User user = findUser(uuid);
        return user.getNickName();
    }

    @Override
    public void updateUser(UUID id,String nickname) {
        User user = findUser(id);
        user.updateUser(nickname);
    }

    @Override
    public List<User> getUserAll() {
        return data.values().stream().toList();
    }

    @Override
    public void userDelete(UUID id) {
        if (data.remove(id) == null) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다. id = " + id);
        }
    }

    private User findUser(UUID id) {
        User user = data.get(id);
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다. id = " + id);
        }
        return user;
    }


}


