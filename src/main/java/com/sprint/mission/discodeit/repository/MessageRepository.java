package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.base.Message;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sprint.mission.discodeit.dto.ChannelLastMessageAt;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

  @EntityGraph(attributePaths = {"author", "author.profile", "author.status"})
  Slice<Message> findAllByChannelId(UUID channelId, Pageable pageable);

  @EntityGraph(attributePaths = {"author", "author.profile", "author.status"})
  Slice<Message> findAllByChannelIdAndCreatedAtLessThan(UUID channelId, Instant cursor,
      Pageable pageable);

  List<Message> findAllByChannelId(UUID channelId);

  Optional<Message> findTopByChannelIdOrderByCreatedAtDesc(UUID id);

  @Query("""
      select new com.sprint.mission.discodeit.dto.ChannelLastMessageAt(
          m.channel.id,
          max(m.createdAt)
      )
      from Message m
      where m.channel.id in :channelIds
      group by m.channel.id
      """)
  List<ChannelLastMessageAt> findLastMessageAtByChannelIds(
      @Param("channelIds") List<UUID> channelIds
  );
}
