package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.*;
import java.util.*;

public interface ChannelService {

  ChannelDto createPublic(PublicChannelCreateRequest request);

  ChannelDto createPrivate(PrivateChannelCreateRequest request);

  ChannelDto addUserToChannel(UUID channelId, UUID userId);

  List<ChannelDto> findAllPublic(UUID id);

  List<ChannelDto> findAllByUserId(UUID userId);

  ChannelDto update(UUID channelId, ChannelUpdateRequest request);

  void delete(UUID id);

  List<UUID> getUserIdsInPublicChannel(UUID channelId);

  void removeUserFromPublicChannel(UUID channelId, UUID userId);

}
