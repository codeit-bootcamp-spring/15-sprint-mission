package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.UUID;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseUpdatableEntity {

  @Column(name = "username", length = 50, nullable = false, unique = true)
  private String username;

  @Column(name = "email", length = 100, nullable = false, unique = true)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  // 프로필 이미지 (ON DELETE SET NULL 정책 대응, ManyToOne 단방향)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "profile_id")
  private BinaryContent profile;

  // 사용자가 생성/삭제될 때 UserStatus도 함께 생명주기를 같이하도록 Cascade 및 orphanRemoval 적용
  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private UserStatus status;

  public User(String username, String email, String password, BinaryContent profile) {
    super();
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
  }

  public User(UUID id, String username, String email, String password, BinaryContent profile) {
    super(id);
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
  }

  public void updateProfile(String username, String email, BinaryContent profile) {
    this.username = username;
    this.email = email;
    this.profile = profile;
  }

  public void updateStatus(UserStatus status) {
    this.status = status;
  }
}