package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity // <-- 필수
@Table(name = "channels")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel extends BaseEntity {

  private String name;
  private String description;
  private ChannelType type;

  public Channel(String name, String description, ChannelType type) {
    super();
    this.name = name;
    this.description = description;
    this.type = type;
  }

  public Channel(UUID id, String name, String description, ChannelType type) {
    super(id);
    this.name = name;
    this.description = description;
    this.type = type;
  }

  public void update(String name, String description) {
    this.name = name;
    this.description = description;
  }
}
