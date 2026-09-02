package com.sprint.mission.discodeit.service.jcf;


import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.util.*;

public class JCFUserService implements UserService
{
    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public User create(String username, String email, String password)
    {
        User user = new User(username, email, password);
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> read(UUID id) {
        // Map에서 id로 조회, 없으면 Optional.empty() 반환
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<User> readALL() {
        // data.values()를 새로운 ArrayList로 복사해 반환 (외부 수정 방지)
        return new ArrayList<>(data.values());
    }

    @Override
    public User update(UUID id, String username, String email, String password) {
        User user = data.get(id);
        if (user != null) {
            // User 내부 update 메서드 호출 (내부에서 updatedAt 갱신)
            user.update(username, email, password);
        }
        return user;
    }

    @Override
    public boolean delete(UUID id) {
        // data.remove(id)는 삭제 성공 시 해당 User 객체를, 키가 없으면 null을 반환
        return data.remove(id) != null;
    }
}
