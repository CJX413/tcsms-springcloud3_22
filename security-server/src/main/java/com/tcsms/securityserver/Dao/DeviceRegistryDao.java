package com.tcsms.securityserver.Dao;



import com.tcsms.securityserver.Entity.DeviceRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

public interface DeviceRegistryDao extends JpaRepository<DeviceRegistry, String>, JpaSpecificationExecutor<DeviceRegistry>, Serializable {

    @Modifying
    @Transactional
    @Query("update DeviceRegistry dr set " +
            "dr.deviceModel = CASE WHEN :#{#deviceRegistry.deviceModel} IS NULL THEN dr.deviceModel ELSE :#{#deviceRegistry.deviceModel} END ," +
            "dr.bigHeight = CASE WHEN :#{#deviceRegistry.bigHeight} IS NULL THEN dr.bigHeight ELSE :#{#deviceRegistry.bigHeight} END ," +
            "dr.bigLength = CASE WHEN :#{#deviceRegistry.bigLength} IS NULL THEN dr.bigLength ELSE :#{#deviceRegistry.bigLength} END ," +
            "dr.smallHeight =  CASE WHEN :#{#deviceRegistry.smallHeight} IS NULL THEN dr.smallHeight ELSE :#{#deviceRegistry.smallHeight} END ," +
            "dr.smallLength =  CASE WHEN :#{#deviceRegistry.smallLength} IS NULL THEN dr.smallLength ELSE :#{#deviceRegistry.smallLength} END ," +
            "dr.rlt =  CASE WHEN :#{#deviceRegistry.rlt} IS NULL THEN dr.rlt ELSE :#{#deviceRegistry.rlt} END ," +
            "dr.latitude =  CASE WHEN :#{#deviceRegistry.latitude} IS NULL THEN dr.latitude ELSE :#{#deviceRegistry.latitude} END ," +
            "dr.longitude =  CASE WHEN :#{#deviceRegistry.longitude} IS NULL THEN dr.longitude ELSE :#{#deviceRegistry.longitude} END " +
            "where dr.deviceId = :#{#deviceRegistry.deviceId}")
    int update(@Param("deviceRegistry") DeviceRegistry deviceRegistry);

//    @Modifying
//    @Transactional
//    @Query("update DeviceRegistry dr set dr.isRegistered=:#{#isRegistered} where dr.deviceId = :#{#deviceId}")
//    int deviceRegister(@Param("deviceId") String deviceId, @Param("isRegistered") String isRegistered);

    List<DeviceRegistry> findByIsRegistered(String isRegistered);
}
