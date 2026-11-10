package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.util.Assert.doesNotContain;

import com.sprint.mission.discodeit.entity.base.Channel;
import com.sprint.mission.discodeit.entity.base.Channel.ChannelType;
import com.sprint.mission.discodeit.entity.base.ReadStatus;
import com.sprint.mission.discodeit.entity.base.User;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class ChannelRepositoryTest {

  @Autowired
  ChannelRepository channelRepository;
  @Autowired
  ReadStatusRepository readStatusRepository;
  @Autowired
  UserRepository userRepository;

  Channel channel1;
  Channel channel2;
  Channel channel3;

  @BeforeEach
  void setUp() {
    // given
    channel1 = channelRepository.save(
        new Channel(ChannelType.PUBLIC, "channel1Name", "channel1Desc"));
    channel2 = channelRepository.save(
        new Channel(ChannelType.PRIVATE, "channel2Name", "channel2Desc"));
    channel3 = channelRepository.save(
        new Channel(ChannelType.PRIVATE, "channel3Name", "channel3Desc"));
  }

  @Test
  @DisplayName("채널 검색 성공")
  void channel_find_success_case() {
    // when
    Optional<Channel> channel1 = channelRepository.findByName("channel1Name");
    List<Channel> channels = channelRepository.findAllByType(ChannelType.PUBLIC);
    // then
    assertThat(channel1)
        .isPresent()
        .get()
        .extracting(Channel::getName)
        .isEqualTo("channel1Name");
    assertThat(channels).extracting(Channel::getName).containsExactly("channel1Name");
  }

  @Test
  @DisplayName("채널 검색 실패")
  void channel_find_fail_case() {
    // when
    Optional<Channel> channel1 = channelRepository.findByName("channel5Name");
    // then
    assertThat(channel1).isEmpty();
  }

  @Test
  @DisplayName("특정 유저 채널 찾기")
  void channel_find_all_by_user() {
    // given
    User user1 = userRepository.save(new User("user1Name", "user1@naver.com", "password", null));
    User user2 = userRepository.save(new User("user2Name", "user2@naver.com", "password", null));
    // when
    readStatusRepository.save(new ReadStatus(user1, channel2, Instant.now()));
    readStatusRepository.save(new ReadStatus(user2, channel3, Instant.now()));

    List<Channel> channels = channelRepository.findVisibleChannels(user1.getId(),
        ChannelType.PUBLIC);

    assertThat(channels)
        .extracting(Channel::getId)
        .containsExactlyInAnyOrder(channel1.getId(), channel2.getId())
        .doesNotContain(channel3.getId());
  }


}