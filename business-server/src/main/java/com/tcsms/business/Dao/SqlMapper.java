package com.tcsms.business.Dao;

import com.tcsms.business.Entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SqlMapper {

    @Select("SELECT * FROM operation_log_${date} WHERE deviceId=#{deviceId} AND time>=#{time} ORDER BY time LIMIT 600")
    List<OperationLog> queryOperationLogDateByDeviceIdAndTimeL600(@Param("deviceId") String deviceId,
                                                              @Param("time") String time,
                                                              @Param("date") String date);


    @Select("SELECT * FROM operation_log_${date} WHERE deviceId=#{deviceId} AND time>#{time} ORDER BY time LIMIT 3600")
    List<OperationLog> queryOperationLogDateByDeviceIdAndTimeL3600(@Param("deviceId") String deviceId,
                                                                     @Param("date") String date,
                                                                     @Param("time") String time);

    @Select("SELECT * FROM operation_log_${date} WHERE deviceId=#{deviceId} ORDER BY time LIMIT 3600")
    List<OperationLog> queryOperationLogDateByDeviceIdAndDateL3600(@Param("deviceId") String deviceId,
                                                              @Param("date") String date);
    @Select("SELECT COUNT(id) FROM operation_log_${date} WHERE deviceId=#{deviceId}")
    int countByDeviceIdOfOperationLogDate(@Param("deviceId") String deviceId,
                                          @Param("date") String date);

}
