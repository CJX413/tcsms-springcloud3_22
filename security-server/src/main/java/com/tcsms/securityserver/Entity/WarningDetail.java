package com.tcsms.securityserver.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@Table(name = "warning_detail")
public class WarningDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "deviceModel")
    private String deviceModel;
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

    @ManyToOne
    @JoinColumn(name = "warningId")
    private WarningLog warningLog;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WarningDetail that = (WarningDetail) o;
        return Double.compare(that.longitude, longitude) == 0 &&
                Double.compare(that.latitude, latitude) == 0 &&
                Double.compare(that.radius, radius) == 0 &&
                Double.compare(that.angle, angle) == 0 &&
                Double.compare(that.height, height) == 0 &&
                Double.compare(that.torque, torque) == 0 &&
                Double.compare(that.weight, weight) == 0 &&
                Double.compare(that.windVelocity, windVelocity) == 0 &&
                magnification == that.magnification &&
                id == that.id &&
                Objects.equals(deviceModel, that.deviceModel) &&
                Objects.equals(deviceId, that.deviceId) &&
                Objects.equals(operator, that.operator) &&
                Objects.equals(workerId, that.workerId) &&
                Objects.equals(time, that.time);
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
        return Objects.hash(deviceModel, deviceId, operator, workerId, time, longitude, latitude, radius, angle, height, torque, weight, windVelocity, magnification, id);
    }
}
