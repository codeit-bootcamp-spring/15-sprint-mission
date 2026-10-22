package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

    @Query("""
        select rs
        from ReadStatus rs
        join fetch rs.user u
        left join fetch u.status
        left join fetch u.profile
        where rs.channel.id = :channelId
        """)
    List<ReadStatus> findAllByChannelIdJoinUser(
            @Param("channelId") UUID channelId
    );

    @Query("""
        select rs
        from ReadStatus rs
        join fetch rs.user u
        left join fetch u.status
        left join fetch u.profile
        where rs.channel.id in :channelIds
        """)
    List<ReadStatus> findAllByChannelIdsJoinUserDetails(
            @Param("channelIds") Collection<UUID> channelIds
    );

    List<ReadStatus> findAllByChannelId(UUID channelId);

    List<ReadStatus> findAllByUserId(UUID userId);


    boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);

    void deleteAllByChannel_Id(UUID channelId);
}
