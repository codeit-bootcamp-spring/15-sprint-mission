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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@EnableJpaAuditing
class ChannelRepositoryTest {
    @Autowired ChannelRepository channelRepository;
    @Autowired EntityManager entityManager;
    UUID memberId;

    @BeforeEach
    void setUp() {
        User member = new User("member@example.com", "password", "member", null);
        entityManager.persist(member);
        memberId = member.getId();
        Channel publicChannel = new Channel("alpha", "public", ChannelType.PUBLIC);
        Channel privateChannel = new Channel("beta", "private", ChannelType.PRIVATE);
        entityManager.persist(publicChannel);
        entityManager.persist(privateChannel);
        entityManager.persist(new ReadStatus(member, privateChannel, Instant.now()));
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("회원에게 공개 채널과 참여 중인 비공개 채널 조회")
    void 채널_커스텀조회_성공() {
        // given
        UUID userId = memberId;

        // when
        var result = channelRepository.findAllVisibleToUser(userId, ChannelType.PUBLIC, ChannelType.PRIVATE);

        // then
        assertThat(result).extracting(Channel::getName).containsExactlyInAnyOrder("alpha", "beta");
    }

    @Test
    @DisplayName("비참여자는 비공개 채널을 조회할 수 없음")
    void 채널_커스텀조회_실패() {
        // given
        UUID otherUserId = UUID.randomUUID();

        // when
        var result = channelRepository.findAllVisibleToUser(otherUserId, ChannelType.PUBLIC, ChannelType.PRIVATE);

        // then
        assertThat(result).extracting(Channel::getName).containsExactly("alpha");
    }


}
