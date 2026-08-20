package com.sprint.mission.discodeit.entity.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.service.UserService;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;

import java.io.Serial;
import java.util.List;
import java.util.UUID;

public class BasicUserService implements UserService {

    // private final JCFUserRepository jcfUserRepository;
    private final FileUserRepository fileUserRepository;

    @Serial
    private static final long serialVersionUID = 1L;

    public BasicUserService() {
        //  this.data = new LinkedHashMap<>(); // 저장 로직
        this.fileUserRepository = new FileUserRepository();
    }

    @Override
    public User createUser(String name, String phoneNum) {
        User user = new User(name, phoneNum);
        fileUserRepository.save(user); // 저장 로직
        return user;
    }

    @Override
    public User getById(UUID id) {
        return fileUserRepository.load(id);
    } // 저장 로직

    @Override
    public List<User> readAll() {
        return fileUserRepository.loadValue();
    } // 저장 로직

    @Override
    public User update(UUID id, String name, String phoneNum) {
        User user = fileUserRepository.load(id); // 저장 로직
        user.update(name, phoneNum);
        // data.put(id, data.get(id)); // 저장 로직
        fileUserRepository.save(user);
        return user;
    }

    @Override
    public boolean deleteById(UUID id) {
        boolean result = fileUserRepository.delete(id); // 저장 로직
        // 삭제 여부를 result에 저장

        return result;
    }
}
