package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.base.User;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  User user1;
  User user2;
  User user3;

  @BeforeEach
  void setUp() {
    // given
    user1 = userRepository.save(new User("user1name", "user1@example.com", "password1", null));
    user2 = userRepository.save(new User("user2name", "user2@example.com", "password2", null));
    user3 = userRepository.save(new User("user3name", "user3@example.com", "password3", null));
  }

  @Test
  @DisplayName("사용자 조회 성공")
  void user_find_success_case() {
    // when
    Optional<User> result1 = userRepository.findByUsername("user1name");
    Optional<User> result2 = userRepository.findByEmail("user2@example.com");
    // then
    assertThat(result1)
        .isPresent()
        .get()
        .extracting(User::getUsername)
        .isEqualTo("user1name");
    assertThat(result2)
        .isPresent()
        .get()
        .extracting(User::getEmail)
        .isEqualTo("user2@example.com");
  }

  @Test
  @DisplayName("사용자 조회 실패")
  void user_find_fail_case() {
    // when
    Optional<User> user1 = userRepository.findByEmail("user4@naver.com");
    Optional<User> user2 = userRepository.findByUsername("user4name");
    // then
    assertThat(user1).isEmpty();
    assertThat(user2).isEmpty();
  }

  @Test
  @DisplayName("이메일 존재 여부 조회 및 사용자명 기준 페이징 정렬")
  void existsByEmail_and_findAll_page_sorted_by_username() {
    // when
    boolean exists = userRepository.existsByEmail("user1@example.com");
    Page<User> page = userRepository.findAll(
        PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "username"))
    );
    // then
    assertThat(exists).isTrue();
    assertThat(page.getContent()).extracting(User::getUsername)
        .containsExactly("user1name", "user2name");
    assertThat(page.getTotalElements()).isEqualTo(3);
  }
}
