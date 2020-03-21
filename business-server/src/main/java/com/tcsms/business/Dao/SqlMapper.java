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

    @Select("SELECT * FROM operation_log_${date} WHERE deviceId=#{deviceId} AND time>=#{time} ORDER BY time LIMIT 100")
    List<OperationLog> queryOperationLogDateByDeviceIdAndTime(@Param("deviceId") String deviceId,
                                                              @Param("time") String time,
                                                              @Param("date") String date);


}
