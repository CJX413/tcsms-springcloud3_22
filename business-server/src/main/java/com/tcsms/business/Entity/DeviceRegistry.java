package com.tcsms.business.Entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Data
@Table(name = "device_registry")
public class DeviceRegistry implements Serializable {
    @Id
    @Column(name = "deviceId")
    private String deviceId;
    @Column(name = "isRegistered", columnDefinition = "bit(1) default 0")
    private Boolean isRegistered;
    @Column(name = "deviceModel")
    private String deviceModel;
    @Column(name = "longitude")
    private Double longitude;
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "rlt")
    private Double rlt;
    @Column(name = "bigHeight")
    private Double bigHeight;
    @Column(name = "smallHeight")
    private Double smallHeight;
    @Column(name = "bigLength")
    private Double bigLength;
    @Column(name = "smallLength")
    private Double smallLength;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceRegistry that = (DeviceRegistry) o;
        return isRegistered == that.isRegistered &&
                Double.compare(that.longitude, longitude) == 0 &&
                Double.compare(that.latitude, latitude) == 0 &&
                Objects.equals(deviceModel, that.deviceModel) &&
                Objects.equals(deviceId, that.deviceId) &&
                Objects.equals(rlt, that.rlt) &&
                Objects.equals(bigHeight, that.bigHeight) &&
                Objects.equals(smallHeight, that.smallHeight) &&
                Objects.equals(bigLength, that.bigLength) &&
                Objects.equals(smallLength, that.smallLength);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isRegistered, deviceModel, deviceId, longitude, latitude, rlt, bigHeight, smallHeight, bigLength, smallLength);
    }

    public String toString() {
        return "{" +
                "\"deviceId\":" + "\"" + deviceId + "\"" + "," +
                "\"isRegistered\":" + isRegistered + "," +
                "\"deviceModel\":" + "\"" + deviceModel + "\"" + "," +
                "\"longitude\":" + longitude + "," +
                "\"latitude\":" + latitude + "," +
                "\"rlt\":" + rlt + "," +
                "\"bigHeight\":" + bigHeight + "," +
                "\"smallHeight\":" + smallHeight + "," +
                "\"bigLength\":" + bigLength + "," +
                "\"smallLength\":" + smallLength +
                "}";
    }
}
