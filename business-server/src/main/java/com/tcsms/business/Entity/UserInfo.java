package com.tcsms.business.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@Table(name = "user_info")
public class UserInfo {
    @Id
    @Column(name = "username")
    private String username;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "workerId")
    private String workerId;
    @Basic
    @Column(name = "phoneNumber")
    private String phoneNumber;
    @Basic
    @Column(name = "sex")
    private String sex;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfo userInfo = (UserInfo) o;
        return Objects.equals(username, userInfo.username) &&
                Objects.equals(name, userInfo.name) &&
                Objects.equals(workerId, userInfo.workerId) &&
                Objects.equals(phoneNumber, userInfo.phoneNumber) &&
                Objects.equals(sex, userInfo.sex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, name, workerId, phoneNumber, sex);
    }

    @Override
    public String toString() {

        return "{" +
                "\"username\":" + "\"" + username + "\"" + "," +
                "\"name\":" + "\"" + name + "\"" + "," +
                "\"workerId\":" + "\"" + workerId + "\"" + "," +
                "\"phoneNumber\":" + "\"" + phoneNumber + "\"" + "," +
                "\"sex\":" + "\"" + sex + "\"" +
                "}";
    }
}
