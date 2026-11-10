package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.entity.base.Channel;
import com.sprint.mission.discodeit.entity.base.Channel.ChannelType;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasicChannelServiceTest {

  @Mock
  ChannelRepository channelRepository;
  @Mock
  UserRepository userRepository;
  @Mock
  ReadStatusRepository readStatusRepository;
  @Mock
  MessageRepository messageRepository;
  @Mock
  BinaryContentRepository binaryContentRepository;
  @Mock
  ChannelMapper channelMapper;
  @Mock
  UserMapper userMapper;
  @InjectMocks
  BasicChannelService channelService;

  @Test
  @DisplayName("Public 채널 생성 성공 케이스")
  void public_channel_create_success_case() {
    // given
    PublicChannelCreateRequest channel = new PublicChannelCreateRequest("channelName",
        "channelDesc");
    Channel savedChannel = new Channel(ChannelType.PUBLIC, channel.name(), channel.description());
    when(channelRepository.save(any(Channel.class))).thenReturn(savedChannel);
    // when
    channelService.createPublic(channel);
    // then
    verify(channelRepository).save(any(Channel.class));
  }

  @Test
  @DisplayName("Private 채널 생성 성공 케이스")
  void private_channel_create_success_case() {
    // given
    User user = new User("username", "user@naver.com", "password", null);
    UUID userId = UUID.randomUUID();
    List<UUID> participantIds = List.of(userId);
    PrivateChannelCreateRequest channel = new PrivateChannelCreateRequest(participantIds);
    Channel savedChannel = new Channel(ChannelType.PRIVATE, null, null);
    when(channelRepository.save(any(Channel.class))).thenReturn(savedChannel);
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    // when
    channelService.createPrivate(channel);
    verify(channelRepository).save(any(Channel.class));
  }


  @Test
  @DisplayName("Private 채널 생성 실패 케이스")
  void private_channel_create_fail_case() {
    // given
    List<UUID> participantIds = List.of(UUID.randomUUID());
    PrivateChannelCreateRequest channel = new PrivateChannelCreateRequest(participantIds);
    // when then
    assertThatThrownBy(() -> channelService.createPrivate(channel)).isInstanceOf(
        UserNotFoundException.class);
  }

  @Test
  @DisplayName("특정 유저 채널 전체 보기")
  void find_channel_all_by_user() {
    // given
    UUID userId = UUID.randomUUID();
    List<Channel> channels = new ArrayList<>();
    Channel channel = new Channel(ChannelType.PUBLIC, "channelName", "channelDesc");
    channels.add(channel);
    ChannelDto expected = new ChannelDto(channel.getId(), ChannelType.PUBLIC, "channelName",
        "channelDesc", List.of(), null);
    when(channelRepository.findVisibleChannels(userId, ChannelType.PUBLIC)).thenReturn(channels);
    when(channelMapper.toDto(channel, List.of(), null)).thenReturn(expected);
    // when
    List<ChannelDto> channelDto = channelService.findAllByUserId(userId);
    // then
    // when(userMapper.toDto(user)).thenReturn(userDto);
    assertThat(channelDto).containsExactly(expected);
  }

  @Test
  @DisplayName("채널 정보 수정 성공 케이스")
  void channel_update_success_case() {
    // given
    UUID channelId = UUID.randomUUID();
    Channel channel = new Channel(ChannelType.PUBLIC, "channelName", "channelDesc");
    ChannelUpdateRequest updateRequest = new ChannelUpdateRequest("newChannelName",
        "newChannelDesc");
    ChannelDto channelDto = new ChannelDto(channelId, ChannelType.PUBLIC, "newChannelName",
        "newChannelDesc", List.of(), null);
    when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
    when(channelMapper.toDto(channel)).thenReturn(channelDto);
    when(channelRepository.save(channel)).thenReturn(channel);
    // when
    ChannelDto updateChannelDto = channelService.update(channelId, updateRequest);
    // then
    assertThat(updateChannelDto).isSameAs(channelDto);
  }

  // 실패할 케이스가 없어서 보류
/*  @Test
  @DisplayName("채널 정보 수정 실패 케이스")
  void channel_update_fail_case(){
    // given
    UUID channelId = UUID.randomUUID();
    Channel channel = new Channel(ChannelType.PUBLIC,"channelName","channelDesc");
    ChannelUpdateRequest request = new ChannelUpdateRequest("channelName",null);
    when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
    // when then
    assertThatThrownBy(()->channelService.update(channelId,request)).isInstanceOf();
  }*/


  @Test
  @DisplayName("채널 삭제 성공 케이스")
  void channel_delete_success_case() {
    // given
    UUID channelId = UUID.randomUUID();
    // when
    channelService.delete(channelId);
    // then
    verify(channelRepository).deleteById(channelId);
  }

  @Test
  @DisplayName("특정 사용자 채널 전체 보기")
  void channel_find_all_by_user() {
    // given
    UUID userId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    List<UUID> channelIds = List.of(channelId);
    Channel channel = new Channel(ChannelType.PUBLIC, "channelName", "channelDesc");
    ChannelDto dto = new ChannelDto(channel.getId(), ChannelType.PUBLIC, "channelName",
        "channelDesc", List.of(), null);
    List<Channel> channels = List.of(channel);
    when(channelRepository.findVisibleChannels(userId, ChannelType.PUBLIC)).thenReturn(channels);
    when(readStatusRepository.findAllByChannelIdIn(anyList())).thenReturn(List.of());
    when(messageRepository.findLastMessageAtByChannelIds(anyList())).thenReturn(List.of());
    when(channelMapper.toDto(channel, List.of(), null)).thenReturn(dto);
    // when
    List<ChannelDto> result = channelService.findAllByUserId(userId);
    // then
    verify(channelRepository).findVisibleChannels(userId, ChannelType.PUBLIC);
    verify(readStatusRepository).findAllByChannelIdIn(anyList());
    verify(messageRepository).findLastMessageAtByChannelIds(anyList());
    verify(channelMapper).toDto(channel, List.of(), null);
    assertThat(result).containsExactly(dto);
  }


}