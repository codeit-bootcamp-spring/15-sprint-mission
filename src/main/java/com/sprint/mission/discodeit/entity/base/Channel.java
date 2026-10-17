package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "channels")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel extends BaseUpdatableEntity {

  @Column(length = 100)
  private String name;

  @Column(length = 500)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(length = 10, nullable = false)
  private ChannelType type;

  public enum ChannelType {PUBLIC, PRIVATE}

  public Channel(ChannelType channelType, String name, String description) {
    this.type = Objects.requireNonNull(channelType);
    this.name = ChannelType.PUBLIC.equals(channelType) ? name : null;
    this.description = ChannelType.PUBLIC.equals(channelType) ? description : null;
  }

  public void update(String name, String description) {
    if (ChannelType.PRIVATE.equals(type)) {
      throw new IllegalArgumentException("PRIVATE 채널은 수정할 수 없습니다.");
    }
    if (name != null) {
      this.name = name;
    }
    if (description != null) {
      this.description = description;
    }
  }
}

