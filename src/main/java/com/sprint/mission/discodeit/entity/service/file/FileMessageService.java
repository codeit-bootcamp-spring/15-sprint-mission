package com.sprint.mission.discodeit.entity.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.service.UserService;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileMessageService implements UserService, Serializable {

    private final Map<UUID, Channel> data;

    @Serial
    private static final long serialVersionUID = 1L;

    public FileMessageService(Map<UUID, Channel> data) {
        this.data = data;
    }

    @Override
    public User createUser(String name, String phoneNum) {
        return null;
    }

    @Override
    public User getById(UUID id) {
        return null;
    }

    @Override
    public List<User> readAll() {
        return List.of();
    }

    @Override
    public User update(UUID id, String name, String phoneNum) {
        return null;
    }

    @Override
    public boolean deleteById(UUID id) {
        return false;
    }
}
