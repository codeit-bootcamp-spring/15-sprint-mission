package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ChannelLastMessageAt;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query("""
        select m from Message m
        left join fetch m.author
        where m.channel.id = :channelId
        """)
    Slice<Message> findAllByChannelIdJoinUser(
            @Param("channelId") UUID channelId, Pageable pageable);


    Slice<Message> findAllByChannelId(UUID channelId, Pageable pageable);
    

    @Query("""
        select max(m.createdAt)
        from Message m
        where m.channel.id = :channelId
        """)
    Optional<Instant> findLastMessageAt(UUID channelId);

    void deleteAllByChannel_Id(UUID channelId);

    @Query("""
    select m.channel.id as channelId,
        max(m.createdAt) as latestMessageAt
        from Message m
        where m.channel.id in :channelIds
        group by m.channel.id
    """)
    List<ChannelLastMessageAt> findLastMessageAtByChannelIds(
            @Param("channelIds") Collection<UUID> channelIds
    );

}
