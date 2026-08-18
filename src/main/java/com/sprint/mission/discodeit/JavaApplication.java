package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCF_UserService;

public class JavaApplication {

    public static void main(String[] args) {

        JCF_UserService userService = new JCF_UserService();

        // 1. 등록
        User user = userService.create(
                "철수",
                "chulsoo@test.com",
                "1234"
        );

        System.out.println("등록: " + user.getName());

        // 2. 단건 조회
        User foundUser = userService.read(user.getId());

        System.out.println("단건 조회: " + foundUser.getName());

        // 3. 다건 조회
        System.out.println("전체 조회: " + userService.readAll());

        // 4. 수정
        userService.update(
                user.getId(),
                "김철수",
                "kim@test.com",
                "5678"
        );

        // 5. 수정된 데이터 조회
        User updatedUser = userService.read(user.getId());

        System.out.println("수정 후 이름: " + updatedUser.getName());

        // 6. 삭제
        userService.delete(user.getId());

        // 7. 삭제 확인
        User deletedUser = userService.read(user.getId());

        System.out.println("삭제 후 조회: " + deletedUser);
    }
}