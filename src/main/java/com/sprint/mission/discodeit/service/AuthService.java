package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.*;
import java.util.*;
public interface AuthService {
    UserResponse login(LoginRequest request);
}
