package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel create(String channelName);          // 생성

    Channel read(UUID id);                       // 하나 조회

    List<Channel> readAll();                     // 전체 조회

    Channel update(UUID id, String channelName); // 수정

    void delete(UUID id);                        // 삭제
}