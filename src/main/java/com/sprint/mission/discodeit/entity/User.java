package com.sprint.mission.discodeit.entity;


import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends Common {

  private String username;
  private String email;
  private String password;
  private UUID profileId;


  public User(String username, String email, String password, UUID profileId) {
    super();
    this.username = username;
    this.email = email;
    this.password = password;
    this.profileId = profileId;
  }
}
