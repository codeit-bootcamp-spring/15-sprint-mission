package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@EnableJpaAuditing
class UserRepositoryTest {
    @Autowired UserRepository userRepository;
    @Autowired EntityManager entityManager;

    @BeforeEach
    void setUp() {
        BinaryContent profile = new BinaryContent("photo.png", 1L, "image/png");
        entityManager.persist(profile);
        User alice = new User("alice@example.com", "password", "alice", profile);
        new UserStatus(alice, Instant.now());
        entityManager.persist(alice);
        entityManager.persist(new User("bob@example.com", "password", "bob", null));
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("프로필과 상태를 포함한 회원 조회 성공")
    void 회원_커스텀조회_성공() {
        // given
        var persistence = Persistence.getPersistenceUtil();

        // when
        var result = userRepository.findAllJoinProfileAndStatus();

        // then
        assertThat(result).hasSize(2);
        User alice = result.stream().filter(user -> user.getUsername().equals("alice")).findFirst().orElseThrow();
        assertThat(persistence.isLoaded(alice, "profile")).isTrue();
        assertThat(persistence.isLoaded(alice, "status")).isTrue();
        assertThat(alice.getProfile().getFileName()).isEqualTo("photo.png");
        assertThat(alice.getStatus()).isNotNull();
        assertThat(alice.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 이름으로 회원 조회 시 빈 결과")
    void 회원_커스텀조회_실패() {
        // given
        String username = "missing";

        // when
        var result = userRepository.findByUsername(username);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("회원명 내림차순 페이징 조회 성공")
    void 회원_페이징정렬_성공() {
        // given
        var pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "username"));

        // when
        var result = userRepository.findAll(pageable);

        // then
        assertThat(result.getContent()).extracting(User::getUsername).containsExactly("bob");
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.hasNext()).isTrue();
    }

    @Test
    @DisplayName("범위를 벗어난 회원 페이지는 빈 결과")
    void 회원_페이징정렬_실패() {
        // given
        var pageable = PageRequest.of(2, 1, Sort.by("username"));

        // when
        var result = userRepository.findAll(pageable);

        // then
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(2);
    }
}
