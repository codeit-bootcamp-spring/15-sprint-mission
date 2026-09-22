package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentUpdateRequest;
import com.sprint.mission.discodeit.dto.exception.ConflictException;
import com.sprint.mission.discodeit.dto.exception.NotFoundException;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  public User create(UserCreateRequest cr, BinaryContentCreateRequest br) {
    UUID profileId = null;

    isDuplicate(cr); // 이메일 | 아이디 중복검사

    if (br != null) {
      BinaryContent binaryContent = new BinaryContent(br.fileName(), br.contentType(), br.bytes());
      binaryContentRepository.save(binaryContent);
      profileId = binaryContent.getId();
    }

    User user = new User(cr.username(), cr.email(), cr.password(), profileId);

    if (userRepository.create(user)) {
      UserStatus userStatus = new UserStatus(user.getId());

      // UserStatus 중복 검사.
      if (userStatusRepository.find(userStatus.getId()) != null) {
        throw new IllegalStateException("User는 미존재로 저장이 가능하나, UserStatus가 이미 존재함");
      }
      userStatusRepository.save(userStatus);
    }

    return user;
  }

  @Override
  public UserResponse find(UUID userId) {
    User user = userRepository.find(userId);
    if (user == null) {
      throw new NotFoundException("user 미존재");
    }

    UserStatus userStatus = userStatusRepository.findByUserId(user.getId());
    if (userStatus == null) {
      throw new NotFoundException("userStatus 미존재");
    }

    return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getProfileId(),
        user.getCreatedAt(), user.getUpdatedAt(), userStatus.isOnline());
  }

  @Override
  public UserResponse findByName(String name) {
    User user = userRepository.findByName(name);
    if (user == null) {
      throw new NotFoundException("user 미존재");
    }

    UserStatus userStatus = userStatusRepository.findByUserId(user.getId());
    if (userStatus == null) {
      throw new NotFoundException("userStatus 미존재");
    }

    return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getProfileId(),
        user.getCreatedAt(), user.getUpdatedAt(), userStatus.isOnline());
  }

  @Override
  public List<UserResponse> findAll() {
    Set<User> users = userRepository.findAll();
    List<UserResponse> userResponses = new ArrayList<>();

    for (User u : users) {
      UserResponse userResponse = new UserResponse(u.getId(), u.getUsername(), u.getEmail(),
          u.getProfileId(), u.getCreatedAt(), u.getUpdatedAt(),
          userStatusRepository.findByUserId(u.getId()).isOnline());
      userResponses.add(userResponse);
    }

    return userResponses;
  }

  @Override
  public User update(UserUpdateRequest updateRequest, BinaryContentUpdateRequest bcur) {
    Set<User> users = userRepository.findAll();
    User user = userRepository.find(updateRequest.id());
    if (user == null) {
      throw new NotFoundException("user 미존재");
    }

    // 이메일 || 아이디 중복 검사.
    for (User u : users) {
      if (u.getId().equals(user.getId())) {
        continue; // 업데이트 될 자기 자신은 제외.
      }
      if (u.getUsername().equals(updateRequest.username()) || u.getEmail()
          .equals(updateRequest.email())) {
        throw new IllegalArgumentException("중복된 이메일또는 아이디 입니다.");
      }
    }

    if (bcur != null) {
      binaryContentRepository.delete(user.getProfileId());
      BinaryContent binaryContent = new BinaryContent(bcur.fileName(), bcur.contentType(),
          bcur.bytes());
      binaryContentRepository.save(binaryContent);
      user.setProfileId(binaryContent.getId());
    }

    user.setUsername(updateRequest.username());
    user.setEmail(updateRequest.email());
    user.setPassword(updateRequest.password());
    user.autoSetUpdatedAt();

    userRepository.update(user);
    System.out.println("정상적으로 업데이트가 되었습니다.");

    return user;
  }

  @Override
  public void delete(UUID userId) {
    User user = userRepository.find(userId);
    if (user == null) {
      System.out.println("유저를 찾을 수 없음.");
      throw new NotFoundException("user 미존재");
    }

    if (binaryContentRepository.find(user.getProfileId()) != null) {
      binaryContentRepository.delete(user.getProfileId());
    }

    if (userStatusRepository.findByUserId(userId) != null) {
      userStatusRepository.delete(userStatusRepository.findByUserId(userId).getId());
    } else {
      throw new NotFoundException("UserStatus가 없음");
    }

    userRepository.delete(userId);
    System.out.println("정상적으로 메세지가 삭제되었습니다.");
  }


  private void isDuplicate(UserCreateRequest cr) {
    Set<User> users = userRepository.findAll();
    UUID profileId = null;

    // 이메일 || 아이디 중복 검사.
    for (User u : users) {
      if (u.getUsername().equals(cr.username()) || u.getEmail().equals(cr.email())) {
        throw new ConflictException("중복된 이메일또는 아이디 입니다.");
      }
    }
  }
}
