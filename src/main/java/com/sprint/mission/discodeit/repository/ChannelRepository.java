package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.base.Channel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sprint.mission.discodeit.entity.base.Channel.ChannelType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, UUID> {

  @Query("""
      select c
      from Channel c
      where c.type = :publicType
         or exists (
             select r.id
             from ReadStatus r
             where r.channel = c
               and r.user.id = :userId
         )
      """)
  List<Channel> findVisibleChannels(
      @Param("userId") UUID userId,
      @Param("publicType") ChannelType publicType
  );

  List<Channel> findAllByType(ChannelType type);
}
