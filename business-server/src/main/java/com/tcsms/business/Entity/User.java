package com.tcsms.business.Entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by echisan on 2018/6/23
 */
@Entity
@Data
@Table(name = "user")
public class User {

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;
    @OneToOne
    @JoinColumn(name="username")
    private UserInfo userInfo;

    @Override
    public String toString() {
        return "{" +
                "'username':" + "\'" + username + "\'" +
                ", 'password':" + "\'" + password + "\'" +
                ", 'role':" + "\'" + role + "\'" +
                "}";
    }
}
