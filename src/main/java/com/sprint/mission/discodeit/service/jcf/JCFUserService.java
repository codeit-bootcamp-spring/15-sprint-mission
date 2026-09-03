package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data;

    public JCFUserService(){
        this.data = new HashMap<>();
    }

    @Override
    public User create(String username, String email) {
        User user = new User(username, email);
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public User find(UUID id){
        return data.get(id);
    }

    @Override
    public List<User> findAll(){
         return new ArrayList<>(data.values());
    }

    @Override
    public User update(UUID id, String username, String email){
        User user = find(id);
        if (user != null) {
            user.update(username, email);
        }
        return user;
    }

    @Override
    public void delete(UUID id){
        data.remove(id);
    }

}
