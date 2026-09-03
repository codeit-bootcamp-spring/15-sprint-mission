package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.entity.User;
import java.util.*;

public interface UserService{
    // public abstract는 interface내의 모든 메소드에 자동 적용임 굳이 안 써도 됨
    User getUser(UUID id);  // 유저 단건 조회(ID 받으면 해당 유저 리턴)
    List<User> getAllUsers();
    User createUser(String userName);

    //User createUser(String userName,);

    User updateUser(UUID id, String userName);
    User deleteUser(UUID id);


}