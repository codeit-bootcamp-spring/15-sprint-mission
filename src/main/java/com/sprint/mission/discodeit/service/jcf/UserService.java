package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.NitroLevel;

public interface UserService {
    void create(String email, String password, String name, NitroLevel nitroLevel);
    void read();
    void update();
    void delete();

}
