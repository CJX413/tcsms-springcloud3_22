package com.tcsms.business.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@Table(name = "operator")
public class Operator {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operator operator = (Operator) o;
        return Objects.equals(username, operator.username) &&
                Objects.equals(specialOperationCertificateNumber, operator.specialOperationCertificateNumber) &&
                Objects.equals(name, operator.name) &&
                Objects.equals(workerId, operator.workerId);
    }

    @Override
    public String toString() {
        return "{" +
                "\"username\":" + "\"" + username + "\"" + "," +
                "\"specialOperationCertificateNumber\":" + "\"" + specialOperationCertificateNumber + "\"" + "," +
                "\"name\":" + "\"" + name + "\"" + "," +
                "\"workerId\":" + "\"" + workerId + "\"" +
                "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, specialOperationCertificateNumber, name, workerId);
    }
}
