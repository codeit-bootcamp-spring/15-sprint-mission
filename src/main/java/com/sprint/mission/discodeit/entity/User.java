package com.sprint.mission.discodeit.entity;


import java.time.Instant;
import java.util.UUID;

//계정,비번,닉,등급?
public class User extends BaseClass{
    private String email;
    private String password;
    private String name;
    private NitroLevel nitroLevel;

    public User() {}
    public User(String email, String password, String name, NitroLevel nitroLevel) {
        super();

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("메일 형식이 아님.");
        }
        this.email=email;
        this.password=password;
        this.name=name;
        this.nitroLevel=nitroLevel;
    }

    public User(UUID id, Long createdAt, Long updatedAt, String email, String password, String name, NitroLevel nitroLevel) {
        super(id, createdAt, updatedAt);
        this.email=email;
        this.password=password;
        this.name=name;
        this.nitroLevel=nitroLevel;
    }




    /*public void setEmail(String email) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {

            throw new IllegalArgumentException("메일 형식이 아님.");
        }

        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNitroLevel(NitroLevel nitroLevel) {
        this.nitroLevel = nitroLevel;
    }*/

    public void update(String email, String password, String name, NitroLevel nitroLevel) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("메일 형식이 아님.");
        }
        this.email=email;
        this.password=password;
        this.name=name;
        this.nitroLevel=nitroLevel;
        setUpdatedAt();
    }



    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }


    public NitroLevel getNitroLevel() {
        return nitroLevel;
    }

    public String ToString(){
        return "User{" +
                "id=" + super.getId() +
                ", createdAt=" + super.getCreatedAt() +
                ", updatedAt=" + super.getUpdatedAt() +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", nitroLevel=" + nitroLevel +
                '}';
    }
}

