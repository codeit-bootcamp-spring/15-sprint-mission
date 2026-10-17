package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    Slice<Message> findAllByChannelId(UUID channelId, Pageable pageable);
    

    @Query("""
    select max(m.createdAt)
    from Message m
    where m.channel.id = :channelId
    """)
    Optional<Instant> findLastMessageAt(UUID channelId);

    void deleteAllByChannel_Id(UUID channelId);
}
