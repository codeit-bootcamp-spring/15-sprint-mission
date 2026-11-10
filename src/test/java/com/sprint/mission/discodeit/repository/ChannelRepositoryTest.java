
package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
class ChannelRepositoryTest {

  @Autowired
  ChannelRepository channelRepository;


  // 1. 공개 채널 조회 성공
  @Test
  void 공개채널_조회_성공() {

    // given - 공개 채널 저장
    Channel channel =
        new Channel(ChannelType.PUBLIC, "공지사항", "공지 채널");

    channelRepository.saveAndFlush(channel);

    // when - 공개 채널 조회
    List<Channel> result =
        channelRepository.findAllByTypeOrIdIn(
            ChannelType.PUBLIC, List.of(UUID.randomUUID()));

    // then - 채널 조회 확인
    assertEquals(1, result.size());
    assertEquals("공지사항", result.get(0).getName());
  }


  // 2. 비공개 채널 ID로 조회 성공
  @Test
  void 비공개채널_ID_조회_성공() {

    // given - 비공개 채널 저장
    Channel channel =
        new Channel(ChannelType.PRIVATE, null, null);

    channelRepository.saveAndFlush(channel);

    // when - 채널 ID로 조회
    List<Channel> result =
        channelRepository.findAllByTypeOrIdIn(
            ChannelType.PUBLIC, List.of(channel.getId()));

    // then - 비공개 채널 조회 확인
    assertEquals(1, result.size());
    assertEquals(channel.getId(), result.get(0).getId());
  }


  // 3. 조건에 맞는 채널이 없는 경우
  @Test
  void 채널_조회_결과없음() {

    // given - 비공개 채널만 저장
    Channel channel =
        new Channel(ChannelType.PRIVATE, null, null);

    channelRepository.saveAndFlush(channel);

    // when - 공개 채널 조회
    List<Channel> result =
        channelRepository.findAllByTypeOrIdIn(
            ChannelType.PUBLIC, List.of(UUID.randomUUID()));

    // then - 빈 목록 확인
    assertTrue(result.isEmpty());
  }


  // 4. 채널 페이징 및 정렬 성공
  @Test
  void 채널_페이징_정렬_성공() {

    // given - 채널 3개 저장
    channelRepository.save(
        new Channel(ChannelType.PUBLIC, "Charlie", "설명"));

    channelRepository.save(
        new Channel(ChannelType.PUBLIC, "Alice", "설명"));

    channelRepository.save(
        new Channel(ChannelType.PUBLIC, "Bob", "설명"));

    PageRequest pageable =
        PageRequest.of(0, 2, Sort.by("name").ascending());

    // when - 페이징 조회
    Page<Channel> result = channelRepository.findAll(pageable);

    // then - 조회 결과 확인
    assertEquals(2, result.getContent().size());
    assertEquals(3, result.getTotalElements());
    assertEquals("Alice", result.getContent().get(0).getName());
    assertEquals("Bob", result.getContent().get(1).getName());
  }


  // 5. 존재하지 않는 페이지 조회
  @Test
  void 채널_페이징_빈목록() {

    // given - 채널 1개 저장
    channelRepository.save(
        new Channel(ChannelType.PUBLIC, "공지사항", "설명"));

    PageRequest pageable = PageRequest.of(2, 10);

    // when - 세 번째 페이지 조회
    Page<Channel> result = channelRepository.findAll(pageable);

    // then - 빈 목록 확인
    assertTrue(result.getContent().isEmpty());
  }
}
