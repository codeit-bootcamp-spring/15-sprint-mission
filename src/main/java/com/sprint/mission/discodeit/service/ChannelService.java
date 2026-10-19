package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.*;
import java.util.*;

public interface ChannelService {

  ChannelResponse createPublic(PublicChannelCreateRequest request);

  ChannelResponse createPrivate(PrivateChannelCreateRequest request);

  ChannelResponse addUserToChannel(UUID channelId, UUID userId);

  List<ChannelResponse> findAllPublic(UUID id);

  List<ChannelResponse> findAllByUserId(UUID userId);

  ChannelResponse update(UUID channelId, ChannelUpdateRequest request);

  void delete(UUID id);

  List<UUID> getUserIdsInPublicChannel(UUID channelId);

  void removeUserFromPublicChannel(UUID channelId, UUID userId);

}
