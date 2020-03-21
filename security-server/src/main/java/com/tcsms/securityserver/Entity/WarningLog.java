package com.tcsms.securityserver.Entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@Table(name = "warning_log")
@EntityListeners(AuditingEntityListener.class)//自动更新时间戳
public class WarningLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "code")
    private int code;
    @Column(name = "message")
    private String message;
    @Column(name = "time")
    @CreatedDate
    private Timestamp time;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "warningLog")
    private List<WarningDetail> warningDetails = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WarningLog that = (WarningLog) o;
        return code == that.code &&
                id == that.id &&
                Objects.equals(message, that.message) &&
                Objects.equals(time, that.time);
    }


    @Override
    public int hashCode() {
        return Objects.hash(code, message, id, time);
    }
}
