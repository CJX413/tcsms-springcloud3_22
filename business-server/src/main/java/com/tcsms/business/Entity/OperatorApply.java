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
@Table(name = "operator_apply")
@EntityListeners(AuditingEntityListener.class)//自动更新时间戳
public class OperatorApply {
    @Id
    @Column(name = "username")
    private String username;
    @Basic
    @Column(name = "specialOperationCertificateNumber")
    private String specialOperationCertificateNumber;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "workerId")
    private String workerId;
    @Basic
    @Column(name = "createTime")
    @CreatedDate
    @LastModifiedDate
    private Timestamp createTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperatorApply that = (OperatorApply) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(specialOperationCertificateNumber, that.specialOperationCertificateNumber) &&
                Objects.equals(name, that.name) &&
                Objects.equals(workerId, that.workerId) &&
                Objects.equals(createTime, that.createTime);
    }

    @Override
    public String toString() {
        return "{" +
                "\"username\":" + "\"" + username + "\"" + "," +
                "\"specialOperationCertificateNumber\":" + "\"" + specialOperationCertificateNumber + "\"" + "," +
                "\"name\":" + "\"" + name + "\"" + "," +
                "\"workerId\":" + "\"" + workerId + "\"" + "," +
                "\"createTime\":" + "\"" + createTime + "\"" +
                "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, specialOperationCertificateNumber, name, workerId, createTime);
    }
}
