package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {
    List<Channel> findAllByTypeEquals(ChannelType type);

    @Query("""
    select c
    from Channel c
    where c.type = :publicType
       or (
           c.type = :privateType
           and exists (
               select rs.id
               from ReadStatus rs
               where rs.channel = c
                 and rs.user.id = :userId
           )
       )
    """)
    List<Channel> findAllVisibleToUser(
            @Param("userId") UUID userId,
            @Param("publicType") ChannelType publicType,
            @Param("privateType") ChannelType privateType
    );





}
