package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.base.Message;
import io.micrometer.observation.ObservationFilter;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

  @EntityGraph(attributePaths = {"author", "author.profile", "author.status"})
  Slice<Message> findAllByChannelId(UUID channelId, Pageable pageable);

  @EntityGraph(attributePaths = {"author", "author.profile", "author.status"})
  Slice<Message> findAllByChannelIdAndCreatedAtLessThan(UUID channelId, Instant cursor,
      Pageable pageable);

  List<Message> findAllByChannelId(UUID channelId);

  Optional<Message> findTopByChannelIdOrderByCreatedAtDesc(UUID id);
}
