package com.tcsms.business.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@Table(name = "building_registry")
public class BuildingRegistry {
    @Id
    @Column(name = "buildingId")
    private String buildingId;
    @Basic
    @Column(name = "pointOneLat")
    private double pointOneLat;
    @Basic
    @Column(name = "pointOneLng")
    private double pointOneLng;
    @Basic
    @Column(name = "pointTwoLat")
    private double pointTwoLat;
    @Basic
    @Column(name = "pointTwoLng")
    private double pointTwoLng;
    @Basic
    @Column(name = "pointThreeLat")
    private double pointThreeLat;
    @Basic
    @Column(name = "pointThreeLng")
    private double pointThreeLng;
    @Basic
    @Column(name = "pointFourLat")
    private double pointFourLat;
    @Basic
    @Column(name = "pointFourLng")
    private double pointFourLng;
    @Basic
    @Column(name = "height")
    private double height;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuildingRegistry that = (BuildingRegistry) o;
        return Double.compare(that.pointOneLat, pointOneLat) == 0 &&
                Double.compare(that.pointOneLng, pointOneLng) == 0 &&
                Double.compare(that.pointTwoLat, pointTwoLat) == 0 &&
                Double.compare(that.pointTwoLng, pointTwoLng) == 0 &&
                Double.compare(that.pointThreeLat, pointThreeLat) == 0 &&
                Double.compare(that.pointThreeLng, pointThreeLng) == 0 &&
                Double.compare(that.pointFourLat, pointFourLat) == 0 &&
                Double.compare(that.pointFourLng, pointFourLng) == 0 &&
                Double.compare(that.height, height) == 0 &&
                Objects.equals(buildingId, that.buildingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(buildingId, pointOneLat, pointOneLng, pointTwoLat, pointTwoLng, pointThreeLat, pointThreeLng, pointFourLat, pointFourLng, height);
    }
}
