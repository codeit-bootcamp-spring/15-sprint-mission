package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.NitroLevel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicUserService implements UserService {
    private UserRepository userRepository;

    private final static BasicUserService instance = new BasicUserService();
    private BasicUserService() { }
    public static BasicUserService getInstance() {
        return instance;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(String email, String password, String name, NitroLevel nitroLevel) {
        User user = new User(email,password,name,nitroLevel);
        return userRepository.save(user);
    }

    @Override
    public User read(UUID id) {
        try{
            return userRepository.findById(id).get();
        }catch (NoSuchElementException e) {
            throw new NoSuchElementException("id 없음");
        }
    }

    @Override
    public List<User> readAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(UUID id, String email, String password, String name, NitroLevel nitroLevel) {
        User user;
        try{
            user = userRepository.findById(id).get();
        }catch (NoSuchElementException e) {
            throw new NoSuchElementException("id 없음");
        }
        finally {

        }
        user.update(email,password,name,nitroLevel);
        return userRepository.save(user);
    }

    @Override
    public void delete(UUID id) {

        try{
            userRepository.findById(id).get();
        }catch (NoSuchElementException e) {
            throw new NoSuchElementException("id 없음");
        }
        userRepository.deleteById(id);
    }
}
