package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto.ChannelFindRequest;
import com.sprint.mission.discodeit.dto.ChannelDto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.ChannelDto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.ChannelDto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    // PUBLIC 채널 생성
    Channel createPublic(PublicChannelCreateRequest request);

    // PRIVATE 채널 생성
    Channel createPrivate(PrivateChannelCreateRequest request);

    // 채널 하나 조회
    ChannelFindRequest find(UUID id);

    // 특정 유저가 볼 수 있는 채널 전체 조회
    List<ChannelFindRequest> findAllByUserId(UUID userId);

    // 채널 수정
    Channel update(UUID id, ChannelUpdateRequest request);

    // 채널 삭제
    void delete(UUID id);
}