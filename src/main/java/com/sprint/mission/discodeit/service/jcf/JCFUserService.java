package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public User create(String username){
        User user = new User(username);
        data.put(user.getId(), user);
        return user;
    }
    @Override
    public User read(UUID id) {
        return data.get(id);
    }

    @Override
    public List<User> readAll(){
        return new ArrayList<>(data.values());
    }
    @Override
    public User update(UUID id, String username){
        User user = data.get(id);
        if (user != null){
            user.update(username);
        }
        return user;
    }
    @Override
    public User delete(UUID id){
        return data.remove(id);
    }
}
