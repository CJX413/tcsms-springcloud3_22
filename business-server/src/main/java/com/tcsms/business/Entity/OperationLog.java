package com.tcsms.business.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "operation_log")
@IdClass(OperationLogPK.class)
public class OperationLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "deviceModel")
    private String deviceModel;
    @Id
    @Column(name = "deviceId")
    private String deviceId;
    @Column(name = "operator")
    private String operator;
    @Column(name = "workerId")
    private String workerId;
    @Column(name = "time")
    private String time;
    @Column(name = "longitude")
    private Double longitude;
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "radius")
    private Double radius;
    @Column(name = "angle")
    private Double angle;
    @Column(name = "height")
    private Double height;
    @Column(name = "torque")
    private Double torque;
    @Column(name = "weight")
    private Double weight;
    @Column(name = "windVelocity")
    private Double windVelocity;
    @Column(name = "magnification")
    private int magnification;

    public OperationLogPK getPk() {
        return new OperationLogPK(id, deviceId);
    }

    public void setPk(OperationLogPK pk) {
        this.id = pk.getId();
        this.deviceId = pk.getDeviceId();
    }

    @Override
    public String toString() {
        return "{" +
                "\"deviceModel\":" + "\"" + deviceModel + "\"" + "," +
                "\"deviceId\":" + "\"" + deviceId + "\"" + "," +
                "\"operator\":" + "\"" + operator + "\"" + "," +
                "\"workerId\":" + "\"" + workerId + "\"" + "," +
                "\"time\":" + "\"" + time + "\"" + "," +
                "\"longitude\":" + longitude + "," +
                "\"latitude\":" + latitude + "," +
                "\"radius\":" + radius + "," +
                "\"angle\":" + angle + "," +
                "\"height\":" + height + "," +
                "\"torque\":" + torque + "," +
                "\"weight\":" + weight + "," +
                "\"windVelocity\":" + windVelocity + "," +
                "\"magnification\":" + magnification +
                "}";
    }
    @Override
    public int hashCode() {
        return Objects.hash(deviceId, height, weight, radius, workerId, magnification, angle);
    }
}
