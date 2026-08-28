package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Channel implements Serializable {
    private UUID id;
    private Long createdAt; // 객채 생성 시간
    private Long updatedAt; // 객체 수정 시간 -> 둘 다 유닉스 타임스탬프로 나타내기위함
    private String channelName;

    /// Jackson 복원용 기본 생성자
    public Channel() {}

    // 생성자 + id, createdAt, updatedAt 초기화
    public Channel(String channelName){
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.channelName = channelName;
    }

    // 각 팔드 반환하는 getter 함수 정의
    public UUID getId() { return id; }
    public String getChannelName() { return channelName; }
    public Long getCreatedAt() { return createdAt; }
    public Long getUpdatedAt() { return updatedAt; }

    // 각 필드 "수정"하는 update 함수 정의
    public String updateChannelName(String channelName) { return this.channelName = channelName; }
    public Long updateUpdatedAt(Long updatedAt) { return this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", channelName='" + channelName + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}' + "\n";
    }

}
