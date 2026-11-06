package com.sprint.mission.discodeit.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.sprint.mission.discodeit.entity.User;
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
@EnableJpaAuditing
class UserRepositoryTest {

  @Autowired
  UserRepository userRepository;

  // 1. 사용자 이름으로 조회 성공
  @Test
  void 사용자_조회_성공() {

    // given
    User user = new User("홍길동", "hong@test.com", "123456", null);
    userRepository.save(user);

    // when
    var result = userRepository.findByUsername("홍길동");

    // then
    assertTrue(result.isPresent());
    assertEquals("홍길동", result.get().getUsername());
  }

  // 2. 사용자 이름으로 조회 실패
  @Test
  void 사용자_조회_실패() {

    // when
    var result = userRepository.findByUsername("없는사용자");

    // then
    assertTrue(result.isEmpty());
  }

  // 3. 이메일 중복 확인 성공
  @Test
  void 이메일_중복_확인() {

    // given
    User user = new User("김철수", "kim@test.com", "123456", null);
    userRepository.save(user);

    // when
    boolean result = userRepository.existsByEmail("kim@test.com");

    // then
    assertTrue(result);
  }

  // 4. 이메일 중복 없음
  @Test
  void 이메일_중복_없음() {

    // when
    boolean result = userRepository.existsByEmail("none@test.com");

    // then
    assertFalse(result);
  }

  // 5. 사용자 이름 중복 확인
  @Test
  void 사용자이름_중복_확인() {

    // given
    User user = new User("홍길동", "hong@test.com", "123456", null);
    userRepository.save(user);

    // when
    boolean result = userRepository.existsByUsername("홍길동");

    // then
    assertTrue(result);
  }

  // 6. 사용자 이름 중복 없음
  @Test
  void 사용자이름_중복_없음() {

    // when
    boolean result = userRepository.existsByUsername("없는사용자");

    // then
    assertFalse(result);
  }

  // 7. 페이징 및 정렬 테스트
  @Test
  void 사용자_페이징_정렬_성공() {

    // given
    userRepository.save(new User("charlie", "c@test.com", "123456", null));
    userRepository.save(new User("alice", "a@test.com", "123456", null));
    userRepository.save(new User("bob", "b@test.com", "123456", null));

    PageRequest pageable =
        PageRequest.of(0, 2, Sort.by("username").ascending());

    // when
    Page<User> result = userRepository.findAll(pageable);

    // then
    assertEquals(2, result.getContent().size());
    assertEquals(3, result.getTotalElements());
    assertEquals("alice", result.getContent().get(0).getUsername());
    assertEquals("bob", result.getContent().get(1).getUsername());
  }
}