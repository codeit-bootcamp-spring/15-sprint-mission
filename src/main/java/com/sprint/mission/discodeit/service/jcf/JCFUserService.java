package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.NitroLevel;
import com.sprint.mission.discodeit.entity.User;

import java.util.*;

public class JCFUserService implements UserService{

    final Map<UUID, User> userMap = new HashMap<>();//UUID = user
    User testUser;//테스트



    private final static JCFUserService instance = new JCFUserService();

    private JCFUserService() { }

    public static JCFUserService getInstance() {
        return instance;
    }




    @Override
    public void create(String email, String password, String name, NitroLevel nitroLevel) {
        User userCreate = new User(email,password,name,nitroLevel);//이메일,비번,닉넴,니트로
        userMap.put(userCreate.getId(),userCreate);
        testUser = userCreate;

    }



    @Override
    public void read() {//테스트용 임시코드
        for (Map.Entry<UUID, User> entry : userMap.entrySet()) {
            System.out.println("ID: " + entry.getKey());
            System.out.println("이름: " + entry.getValue().getName());
            System.out.println("이메일: " + entry.getValue().getEmail());
            System.out.println("수정시간: " + entry.getValue().getUpdatedAt());
        }

    }



    @Override
    public void update(UUID id, String email, String password, String name, NitroLevel nitroLevel) {
        if(!userMap.containsKey(id)){
            throw new IllegalArgumentException("해당 id가 없습니다.");
        }
        userMap.get(id).update(email,password,name,nitroLevel);

    }

    @Override
    public void delete(UUID id) {
        userMap.remove(id);
        JCFMessageService.getInstance().delete_UserToMessage(id);
        JCFChannelService.getInstance().delete_UserToChannel(id);
    }


    //////////////////////////////////////////////////
    public User getTestUser() {
        return testUser;
    }//테스트
}
