package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class User /*implements Serializable*/ {
    private /*final*/ UUID id;      // UUID형식의 랜덤 값
    private /*final*/ Long createdAt; // 객채 생성 시간
    private Long updatedAt; // 객체 수정 시간 -> 둘 다 유닉스 타임스탬프로 나타내기위함
    private String userName;

    /// Jackson 복원용 기본 생성자
    public User() {}

    public User(String name){
        this.id = UUID.randomUUID();    // ID값 랜덤 생성
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;    //
        this.userName = name;
    }

    public UUID getId() { return id; }
    public Long getCreatedAt() { return createdAt; }
    public Long getUpdatedAt() { return updatedAt; }
    public String getUserName() { return userName; }


    // id랑 createdAt은 바뀌면 안됨
    public Long updateUpdatedAt(Long updatedAt) { return this.updatedAt = updatedAt; }
    public String updateName(String name) { return this.userName = name; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}' + "\n";
    }
}
