package com.sprint.mission.discodeit.entity;

import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Message extends Common {

  private final UUID channelId;
  private final UUID authorId;
  private final List<UUID> attachmentIds; //binaryContent의 id
  private String content;

  public Message(String content, UUID channelId, UUID authorId, List<UUID> attachmentIds) {
    super();
    this.content = content;
    this.channelId = channelId;
    this.authorId = authorId;
    this.attachmentIds = attachmentIds;
  }


  public void setMessage(String content) {
    this.content = content;
  }
}
