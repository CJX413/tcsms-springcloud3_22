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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @Override
    public String toString() {
        return "{" +
                "'id':" + id +
                ", 'username':" + "\'" + username + "\'" +
                ", 'password':" + "\'" + password + "\'" +
                ", 'role':" + "\'" + role + "\'" +
                "}";
    }
}
