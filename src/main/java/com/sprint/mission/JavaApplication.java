package com.sprint.mission;
import com.sprint.mission.discodeit.entity.User;   // ← 이 줄
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class JavaApplication {
    public static void main(String[] args) {
        JCFUserService userService = new JCFUserService();

// Create
        User user1 = userService.create("codeit");

// Read
// Update
        userService.update(user1.getId(), "codeit123");
// Delete
        userService.delete(user1.getId());
    }
}
