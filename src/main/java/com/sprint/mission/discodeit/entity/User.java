package com.sprint.mission.discodeit.entity;


import jakarta.persistence.*;
import lombok.Getter;


@Table(name = "users")
@Entity
@Getter
public class User extends BaseUpdatableEntity {

    @Column(nullable = false,unique = true,length = 100)
    private String email;

    @Column(nullable = false,length = 60)
    private String password;

    @Column(nullable = false, unique = true,length = 50)
    private String username;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id",unique = true)
    private BinaryContent profile;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private UserStatus status;


    protected User(){}

    public User(String email, String password, String username, BinaryContent profile) {
        super();
        this.email=email;
        this.password=password;
        this.username = username;
        this.profile=profile;
    }

    public void update(String email, String password, String name , BinaryContent profile) {

        this.email=email;
        this.password=password;
        this.username =name;
        this.profile=profile;
    }
    public void setStatus(UserStatus status) {
        this.status = status;
    }
}

