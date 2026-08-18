package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Reaction;
import com.sprint.mission.discodeit.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

//반응 숫자 맵
public class JCFMessageService implements MessageService{
    final Map<UUID, Message>  messageMap = new HashMap<>();
    final Map<UUID ,Map<Reaction, Set<User>>> reactionMap = new HashMap<>();
    //2nf?
    private final static JCFMessageService instance = new JCFMessageService();

    private JCFMessageService() { }

    public static JCFMessageService getInstance() {
        return instance;
    }


    @Override
    public void create() {

    }

    @Override
    public void read() {

    }

    @Override
    public void update() {

    }

    @Override
    public void delete() {

    }
}
