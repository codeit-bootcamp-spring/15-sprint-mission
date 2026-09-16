package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.*;
import java.util.*;
public interface ChannelService {
    ChannelResponse createPublic(PublicChannelCreateRequest request);
    ChannelResponse createPrivate(PrivateChannelCreateRequest request);
    void addUserToChannel(UUID channelId, UUID userId);
    ChannelResponse find(UUID id);
    List<ChannelResponse> findAllPublic();
    List<ChannelResponse> findAllByUserId(UUID userId);
    ChannelResponse update(ChannelUpdateRequest request);
    void delete(UUID id);
    List<UUID> getUserIdsInPublicChannel(UUID channelId);
    void removeUserFromPublicChannel(UUID channelId, UUID userId);

}
