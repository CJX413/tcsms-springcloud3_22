package com.tcsms.securityserver.Entity;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

@Data
public class OperationLogPK implements Serializable {
    private static final long serialVersionUID = 8764734968205185910L;
    @Column(name = "deviceId")
    private String deviceId;
    @Column(name = "id")
    private Integer id;

    public OperationLogPK(){

    }
    public OperationLogPK(int id, String deviceId) {
        this.id = id;
        this.deviceId = deviceId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationLogPK that = (OperationLogPK) o;
        return id == that.id &&
                Objects.equals(deviceId, that.deviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceId, id);
    }
}
