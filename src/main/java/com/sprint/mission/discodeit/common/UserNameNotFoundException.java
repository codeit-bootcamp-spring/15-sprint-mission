package com.sprint.mission.discodeit.common;

public class UserNameNotFoundException extends RuntimeException{
    public UserNameNotFoundException(String username) {
        super("Username: " + username + "찾을 수 없음.");
    }
}
