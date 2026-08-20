package com.sprint.mission.discodeit.entity.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.service.UserService;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;

import java.util.*;

public class JCFUserService implements UserService {
    // private final Map<UUID, User> data;
    private JCFUserRepository jcfUserRepository;
    public JCFUserService() {
        this.jcfUserRepository = new JCFUserRepository();
        // this.data = new LinkedHashMap<>(); // 저장 로직
    }

    // data 필드를 활용해 생성, 조회, 수정, 삭제하는 메소드를 구현하세요.
    @Override
    public User createUser(String name, String phoneNum){
        User user = new User(name, phoneNum);
        // data.put(user.getId(), user); // 저장 로직
        jcfUserRepository.save(user);
        return user;
    }

    @Override
    public User getById(UUID id) {
        return jcfUserRepository.load(id);
    } // 저장 로직

    @Override
    public List<User> readAll() {
        // 데이터의 밸류값이 리스트 형태로 저장됨.
        return jcfUserRepository.loadValue(); // 저장 로직
        // values를 쓰면 map에서 value만 분리돼서 컬렉션으로 저장된다.
        // 그 컬렉션을 새 arraylist 객체로 만든다.
    }

    @Override
    public User update(UUID id, String name, String phoneNum) {
        // 수정 : 조회 + 생성
        User user = jcfUserRepository.load(id); // 저장 로직
        user.update(name, phoneNum);
        jcfUserRepository.save(user); // 저장 로직
        return user;
    }

    @Override
    public boolean deleteById(UUID id) {
        return jcfUserRepository.delete(id);
    } // 저장 로직




}
