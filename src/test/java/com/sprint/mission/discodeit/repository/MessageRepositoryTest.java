package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@EnableJpaAuditing
class MessageRepositoryTest {
    @Autowired MessageRepository messageRepository;
    @Autowired EntityManager entityManager;
    UUID channelId;
    Instant latest = Instant.parse("2026-01-03T00:00:00Z");

    @BeforeEach
    void setUp() {
        User user = new User("user@example.com", "password", "user", null);
        Channel channel = new Channel("test", "description", ChannelType.PUBLIC);
        Channel other = new Channel("other", "description", ChannelType.PUBLIC);
        entityManager.persist(user);
        entityManager.persist(channel);
        entityManager.persist(other);
        channelId = channel.getId();
        for (int i = 0; i < 3; i++) {
            Message message = new Message(channel, user, "message-" + i, List.of());
            entityManager.persist(message);
            entityManager.flush();
            entityManager.createQuery("update Message m set m.createdAt = :time where m.id = :id")
                    .setParameter("time", latest.minusSeconds(i))
                    .setParameter("id", message.getId()).executeUpdate();
        }
        entityManager.persist(new Message(other, user, "other channel", List.of()));
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("생성시간 내림차순 커서 페이징 조회")
    void 메시지_커서페이징_성공() {
        // given
        var pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"));

        // when
        var first = messageRepository.findAllByChannelIdWithCursor(channelId, null, pageable);
        var second = messageRepository.findAllByChannelIdWithCursor(channelId,
                first.getContent().get(1).getCreatedAt(), pageable);

        // then
        assertThat(first.getContent()).extracting(Message::getContent).containsExactly("message-0", "message-1");
        assertThat(first.hasNext()).isTrue();
        assertThat(second.getContent()).extracting(Message::getContent).containsExactly("message-2");
        assertThat(second.hasNext()).isFalse();
        assertThat(messageRepository.countByChannelId(channelId)).isEqualTo(3);
    }

    @Test
    @DisplayName("커서 이전에 메시지가 없으면 빈 결과")
    void 메시지_커서페이징_실패() {
        // given
        var pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"));

        // when
        var result = messageRepository.findAllByChannelIdWithCursor(channelId, latest.minusSeconds(2), pageable);

        // then
        assertThat(result.getContent()).isEmpty();
        assertThat(result.hasNext()).isFalse();
    }

    @Test
    @DisplayName("채널의 최신 메시지 생성시간 조회")
    void 최신메시지_조회_성공() {
        // given
        UUID id = channelId;

        // when
        var result = messageRepository.findLastMessageAt(id);

        // then
        assertThat(result).contains(latest);
    }

    @Test
    @DisplayName("메시지가 없는 채널의 최신시간 조회는 빈 결과")
    void 최신메시지_조회_실패() {
        // given
        UUID missingId = UUID.randomUUID();

        // when
        var result = messageRepository.findLastMessageAt(missingId);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("채널별 최신 메시지 시간 집계 조회")
    void 채널별_집계_성공() {
        // given
        var channelIds = List.of(channelId);

        // when
        var result = messageRepository.findLastMessageAtByChannelIds(channelIds);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getChannelId()).isEqualTo(channelId);
        assertThat(result.get(0).getLatestMessageAt()).isEqualTo(latest);
    }

    @Test
    @DisplayName("대상 채널에 메시지가 없으면 집계 결과 없음")
    void 채널별_집계_실패() {
        // given
        var channelIds = List.of(UUID.randomUUID());

        // when
        var result = messageRepository.findLastMessageAtByChannelIds(channelIds);

        // then
        assertThat(result).isEmpty();
    }
}
