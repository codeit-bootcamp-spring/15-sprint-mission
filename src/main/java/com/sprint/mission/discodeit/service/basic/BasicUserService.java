package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserReadRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public User create(UserCreateRequest cr, BinaryContentCreateRequest br) {
        Set<User> users = userRepository.findAll();
        UUID profileId = null;

        // 이메일 || 아이디 중복 검사.
        for (User u : users) {
            if (u.getUsername().equals(cr.username()) || u.getEmail().equals(cr.email())) {
                System.out.println("[중복된 이메일또는 아이디 입니다]" + u.getEmail() + " " + u.getUsername());
                System.out.println("이메일: " + u.getEmail());
                System.out.println("아이디: " + u.getUsername());

                return null;
            }
        }


        if (br != null) {
            BinaryContent binaryContent = new BinaryContent(br.fileName(), br.contentType(), br.bytes());
            if (!binaryContentRepository.save(binaryContent)) {
                System.out.println("파일이 저장되지 않았습니다.");
            }
            profileId = binaryContent.getId();
        }

        User user = new User(cr.username(), cr.email(), cr.password(), profileId);

        if (userRepository.create(user)) {
            return user;
        }

        UserStatus userStatus = new UserStatus(user.getId());
        // 중복 검사. 안찾아지는 경우에만 저장.
        if (userStatusRepository.find(userStatus.getId()) == null) {
            userStatusRepository.save(userStatus);
        }

        System.out.println("알 수 없는 이유로 생성에 실패했습니다.");
        return null;
    }

    @Override
    public UserReadRequest find(UUID userId) {
        User user = userRepository.find(userId);
        if (user == null) {
            return null;
        }

        UserStatus userStatus = userStatusRepository.find(user.getId());
        if (userStatus == null) {
            return null;
        }


        return new UserReadRequest(user.getUsername(), user.getEmail(), user.getProfileId(), userStatus.isOnline());
    }

    @Override
    public UserReadRequest findByName(String name) {
        User user = userRepository.findByName(name);
        if (user == null) {
            return null;
        }

        UserStatus userStatus = userStatusRepository.find(user.getId());
        if (userStatus == null) {
            return null;
        }


        return new UserReadRequest(user.getUsername(), user.getEmail(), user.getProfileId(), userStatus.isOnline());
    }

    @Override
    public List<UserReadRequest> findAll() {
        Set<User> users = userRepository.findAll();
        List<UserReadRequest> userReadRequests = new ArrayList<>();
        for (User u : users) {

            UserReadRequest userReadRequest = new UserReadRequest(u.getUsername(), u.getEmail(), u.getProfileId(),
                    userStatusRepository.find(u.getId()).isOnline());
           userReadRequests.add(userReadRequest);
        }

        return userReadRequests;
    }

    @Override
    public void update(UUID userId, UserUpdateRequest updateRequest, BinaryContentUpdateRequest bcur) {
        User user = userRepository.find(userId);
        if (user == null) {
            System.out.println("유저를 찾을 수 없음.");
            return;
        }

        if (bcur != null) {
            binaryContentRepository.delete(user.getProfileId());
            BinaryContent binaryContent = new BinaryContent(bcur.fileName(), bcur.contentType(), bcur.bytes());
            binaryContentRepository.save(binaryContent);
            user.setProfileId(binaryContent.getId());
        }

        // TODO: NULL로 업데이트를 허용할건지 말건지... 도 고려해봐야 할듯?
        user.setUsername(updateRequest.username());
        user.setEmail(updateRequest.email());
        user.setPassword(updateRequest.password());
        user.autoSetUpdatedAt();

        if (userRepository.update(user)) {
            System.out.println("정상적으로 업데이트가 되었습니다.");
        }
        else {
            System.out.println("오류가 발생하여 업데이트가 되지 않았습니다.");
        }
    }

    @Override
    public void delete(UUID id) {
        if (binaryContentRepository.find(id) != null) {
            binaryContentRepository.delete(id);
        }

        if (userRepository.delete(id)) {
            if (userStatusRepository.delete(id)) {
                System.out.println("정상적으로 메세지가 삭제되었습니다.");
                return;
            }
        }

        System.out.println("오류가 발생하여 메세지가 삭제되지 않았습니다.");

    }
}
