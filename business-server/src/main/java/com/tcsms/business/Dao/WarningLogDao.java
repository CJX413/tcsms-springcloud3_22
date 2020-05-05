package com.tcsms.business.Dao;


import com.tcsms.business.Entity.WarningLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;

public interface WarningLogDao extends JpaRepository<WarningLog, String>, JpaSpecificationExecutor<WarningLog>, Serializable {
    @Query(value = "SELECT * FROM warning_log LEFT JOIN warning_detail ON (warning_log.id=warning_detail.warningId AND warning_log.time like :time%) WHERE warning_detail.deviceId=:deviceId", nativeQuery = true)
    List<WarningLog> findByDeviceIdAndTime(@Param("deviceId") String deviceId, @Param("time") String time);

    @Query(value = "SELECT warning_log.message as 'message', COUNT(warning_log.code) as 'count' FROM  warning_log LEFT JOIN warning_detail ON (warning_log.id=warning_detail.warningId AND warning_log.time like :time%) WHERE warning_detail.deviceId=:deviceId GROUP BY warning_log.code", nativeQuery = true)
    List<Object[]> countByCodeAndTime(@Param("deviceId") String deviceId, @Param("time") String time);
}
