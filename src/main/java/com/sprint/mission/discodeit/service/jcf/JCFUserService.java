package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.NitroLevel;
import com.sprint.mission.discodeit.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFUserService implements UserService{

    final Map<UUID, User> userMap = new HashMap<>();


    private final static JCFUserService instance = new JCFUserService();

    private JCFUserService() { }

    public static JCFUserService getInstance() {
        return instance;
    }


    @Override
    public void create(String email, String password, String name, NitroLevel nitroLevel) {
        User user = new User(email,password,name,nitroLevel);//이메일,비번,닉넴,니트로
        userMap.put(user.getId(),user);

    }



    @Override
    public void read() {//테스트용 임시코드
        for (Map.Entry<UUID, User> entry : userMap.entrySet()) {
            System.out.println("ID: " + entry.getKey());
            System.out.println("이름: " + entry.getValue().getName());
            System.out.println("이메일: " + entry.getValue().getEmail());
        }

    }

    @Override
    public void update() {

    }

    @Override
    public void delete() {

    }
}
