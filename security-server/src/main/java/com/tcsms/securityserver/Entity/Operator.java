package com.tcsms.securityserver.Entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Data
public class Operator {
    @Id
    @Column(name = "username")
    private String username;
    @Column(name = "specialOperationCertificateNumber")
    private String specialOperationCertificateNumber;
    @Column(name = "name")
    private String name;
    @Column(name = "workerId")
    private String workerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operator operator = (Operator) o;
        return Objects.equals(username, operator.username) &&
                Objects.equals(specialOperationCertificateNumber, operator.specialOperationCertificateNumber) &&
                Objects.equals(name, operator.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, specialOperationCertificateNumber, name);
    }

}
