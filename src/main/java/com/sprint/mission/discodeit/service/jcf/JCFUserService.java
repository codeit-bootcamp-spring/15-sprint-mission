package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;


import java.io.IOException;
import java.util.*;

public class JCFUserService implements UserService {

    //User 객체들을 메모리에 저장하는 리스트(JCF = Java
    private final List<User> data;

    public JCFUserService() {
        this.data = new ArrayList<>();

    }


    @Override
    public User create(User user) {
        data.add(user);
        return user;
    }

    @Override
    public User create(String username, String email, String password) throws IOException {
        return null;
    }

    @Override
    public Optional<User> findById(UUID id) {
        for (User user : data) {
            if (user.getId().equals(id)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return data;
    }

    @Override
    public Optional<Object> update(UUID id, String name) {
        Optional<User> optionalUser = findById(id);
        optionalUser.ifPresent(user -> user.update(name));
        return Optional.of(optionalUser);
    }



    @Override
    public void delete(UUID id) throws IOException {
        findById(id).ifPresent(data::remove);

    }

    @Override
    public List<User> findall() {
        return List.of();
    }

}









