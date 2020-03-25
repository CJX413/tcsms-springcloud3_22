package com.tcsms.business.Entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Data
@Table(name = "role_apply")
@EntityListeners(AuditingEntityListener.class)//自动更新时间戳
public class RoleApply {
    @Id
    @Column(name = "username")
    private String username;
    @Basic
    @Column(name = "role")
    private String role;
    @Basic
    @Column(name = "createTime")
    @CreatedDate
    @LastModifiedDate
    private Timestamp createTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleApply roleApply = (RoleApply) o;
        return Objects.equals(username, roleApply.username) &&
                Objects.equals(role, roleApply.role) &&
                Objects.equals(createTime, roleApply.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, role, createTime);
    }

    @Override
    public String toString() {
        return "{" +
                "\"username\":" + "\"" + username + "\"" + "," +
                "\"role\":" + "\"" + role + "\"" + "," +
                "\"createTime\":" + "\"" + createTime + "\"" +
                "}";
    }
}
