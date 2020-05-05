package com.tcsms.business.Dao;

import com.tcsms.business.Entity.OperationLog;
import com.tcsms.business.Entity.OperationLogPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;

public interface OperationLogDao extends JpaRepository<OperationLog, OperationLogPK>, JpaSpecificationExecutor<OperationLog>, Serializable {
    @Query(value = "SELECT * FROM operation_log WHERE operation_log.deviceId=:deviceId AND operation_log.time>:time ORDER BY operation_log.time LIMIT 3600", nativeQuery = true)
    List<OperationLog> queryOperationLogByDeviceIdAndTimeL3600(@Param("deviceId") String deviceId, @Param("time") String time);

    @Query(value = "SELECT * FROM operation_log WHERE operation_log.deviceId=:deviceId ORDER BY operation_log.time LIMIT 3600", nativeQuery = true)
    List<OperationLog> queryOperationLogByDeviceId(@Param("deviceId") String deviceId);

    int countByDeviceId(@Param("deviceId") String deviceId);
    @Query(value = "SELECT * FROM operation_log WHERE operation_log.deviceId=:deviceId AND operation_log.time>:time ORDER BY operation_log.time LIMIT 600", nativeQuery = true)
    List<OperationLog> queryOperationLogByDeviceIdAndTimeL600(@Param("deviceId") String deviceId, @Param("time") String time);

}
