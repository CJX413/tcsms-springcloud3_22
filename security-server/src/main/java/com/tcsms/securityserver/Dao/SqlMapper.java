package com.tcsms.securityserver.Dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface SqlMapper {
    /**
     * 这些是用来刷新每天的设备运行日志表的操作
     */
    @Update("CREATE TABLE operation_log_clone LIKE operation_log")
    void cloneTabOfOperationLog();

    @Update("ALTER TABLE operation_log_clone RENAME TO operation_log")
    void renameOperationLogCloneToOperationLog();

    @Update("ALTER TABLE operation_log RENAME TO operation_log_${date}")
    void renameOperationLogToOperationLogDate(@Param("date") String date);

    @Update("ALTER TABLE operation_log REMOVE PARTITIONING;")
    void removePartitioningOfOperationLog();

    @Update("ALTER TABLE operation_log PARTITION BY KEY(deviceId) PARTITIONS 127;")
    void createPartitioningOfOperationLog();

    /**
     * 这些事用来在刷新operation_log表出错时的操作
     */
    @Update("CREATE TABLE operation_log_backup LIKE operation_log;")
    void createOperationLogBackup();

    @Insert("INSERT INTO operation_log_backup SELECT * FROM operation_log;")
    int backupOperationLog();

    @Delete("drop table if exists operation_log_backup;")
    void dropOperationLogBackup();

    @Delete("drop table if exists operation_log_clone;")
    void dropOperationLogClone();

    @Delete("drop table if exists operation_log;")
    void dropOperationLog();

    @Delete("drop table if exists operation_log_${date};")
    void dropOperationLogDate(@Param("date") String date);

    @Update("CREATE TABLE operation_log SELECT * FROM operation_log_backup")
    void restoreOperationLog();

}
