package com.sprint.mission.discodeit.integration;

import com.jayway.jsonpath.JsonPath;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserApiIntegrationTest {
    @Autowired MockMvc mockMvc;
    @Autowired UserRepository userRepository;
    @Autowired UserStatusRepository userStatusRepository;
    @Autowired EntityManager entityManager;

    @Test
    @DisplayName("회원가입 API가 회원과 온라인 상태를 DB에 저장")
    void 회원생성_성공() throws Exception {
        // given
        var request = jsonPart("userCreateRequest",
                "{\"email\":\"new@example.com\",\"password\":\"password\",\"username\":\"newuser\"}");

        // when
        var result = mockMvc.perform(multipart("/api/users").file(request));

        // then
        result.andExpect(status().isCreated()).andExpect(jsonPath("$.username").value("newuser"));
        String id = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.id");
        entityManager.flush();
        entityManager.clear();
        User saved = userRepository.findById(UUID.fromString(id)).orElseThrow();
        assertThat(saved.getEmail()).isEqualTo("new@example.com");
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(userStatusRepository.findByUserId(saved.getId())).isPresent();
    }

    @Test
    @DisplayName("중복 이름으로 회원가입 시 409 반환")
    void 회원생성_실패() throws Exception {
        // given
        userRepository.saveAndFlush(new User("existing@example.com", "password", "existing", null));
        var request = jsonPart("userCreateRequest",
                "{\"email\":\"new@example.com\",\"password\":\"password\",\"username\":\"existing\"}");

        // when
        var result = mockMvc.perform(multipart("/api/users").file(request));

        // then
        result.andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(ErrorCode.DUPLICATE_USER_NAME.getCode()));
    }

    @Test
    @DisplayName("회원 수정 API가 변경값을 DB에 반영하고 생략한 비밀번호 유지")
    void 회원수정_성공() throws Exception {
        // given
        User user = userRepository.saveAndFlush(new User("user@example.com", "password", "user", null));
        var request = jsonPart("userUpdateRequest", "{\"newUsername\":\"updated\"}");

        // when
        var result = mockMvc.perform(multipart(HttpMethod.PATCH, "/api/users/{id}", user.getId()).file(request));

        // then
        result.andExpect(status().isOk()).andExpect(jsonPath("$.username").value("updated"));
        entityManager.flush();
        entityManager.clear();
        User saved = userRepository.findById(user.getId()).orElseThrow();
        assertThat(saved.getUsername()).isEqualTo("updated");
        assertThat(saved.getPassword()).isEqualTo("password");
    }

    @Test
    @DisplayName("없는 회원 수정 시 404 반환")
    void 회원수정_실패() throws Exception {
        // given
        UUID id = UUID.randomUUID();
        var request = jsonPart("userUpdateRequest", "{\"newUsername\":\"updated\"}");

        // when
        var result = mockMvc.perform(multipart(HttpMethod.PATCH, "/api/users/{id}", id).file(request));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.getCode()));
    }

    @Test
    @DisplayName("회원 삭제 API가 회원과 연관 상태를 삭제")
    void 회원삭제_성공() throws Exception {
        // given
        User user = new User("user@example.com", "password", "user", null);
        new UserStatus(user, Instant.now());
        userRepository.saveAndFlush(user);
        UUID id = user.getId();

        // when
        var result = mockMvc.perform(delete("/api/users/{id}", id));

        // then
        result.andExpect(status().isNoContent());
        entityManager.flush();
        entityManager.clear();
        assertThat(userRepository.findById(id)).isEmpty();
        assertThat(userStatusRepository.findByUserId(id)).isEmpty();
    }

    @Test
    @DisplayName("없는 회원 삭제 시 404 반환")
    void 회원삭제_실패() throws Exception {
        // given
        UUID id = UUID.randomUUID();

        // when
        var result = mockMvc.perform(delete("/api/users/{id}", id));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.details.userId").value(id.toString()));
    }

    @Test
    @DisplayName("DB에 저장한 회원 목록을 JSON으로 조회")
    void 회원목록_조회_성공() throws Exception {
        // given
        userRepository.saveAndFlush(new User("user@example.com", "password", "user", null));
        entityManager.clear();

        // when
        var result = mockMvc.perform(get("/api/users"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].email").value("user@example.com"));
    }

    @Test
    @DisplayName("등록된 회원이 없으면 빈 목록 반환")
    void 회원목록_조회_결과없음() throws Exception {
        // given
        assertThat(userRepository.count()).isZero();

        // when
        var result = mockMvc.perform(get("/api/users"));

        // then
        result.andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    private MockMultipartFile jsonPart(String name, String json) {
        return new MockMultipartFile(name, "", "application/json", json.getBytes(StandardCharsets.UTF_8));
    }
}
