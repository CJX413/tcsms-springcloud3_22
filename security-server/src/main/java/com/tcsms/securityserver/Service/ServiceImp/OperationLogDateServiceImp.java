package com.tcsms.securityserver.Service.ServiceImp;

import com.tcsms.securityserver.Config.ConstantConfig;
import com.tcsms.securityserver.Dao.DeviceRegistryDao;
import com.tcsms.securityserver.Dao.OperationLogDao;
import com.tcsms.securityserver.Dao.SqlMapper;
import com.tcsms.securityserver.Entity.DeviceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class OperationLogDateServiceImp {
    @Autowired
    SqlMapper sqlMapper;
    @Autowired
    DeviceRegistryDao deviceRegistryDao;
    @Autowired
    OperationLogDao operationLogDao;
    private final static int TRY_TIMES = 5;
    private int backupTimes = 0;
    private int refreshTimes = 0;
    private int repairTimes = 0;

    public SqlMapper getMapper() {
        return sqlMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean backupOperationLog() {
        boolean success = true;
        try {
            sqlMapper.dropOperationLogBackup();
            sqlMapper.createOperationLogBackup();
            sqlMapper.backupOperationLog();
        } catch (RuntimeException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//关键
            e.printStackTrace();
            if (backupTimes > TRY_TIMES)
                return false;
            backupTimes++;
            success = this.backupOperationLog();
        }
        return success;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean refreshOperationLogTab() {
        boolean success = true;
        try {
            sqlMapper.cloneTabOfOperationLog();
            System.out.println("克隆表结构成功");
            SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd");
            String dateTime = df.format(new Date());
            sqlMapper.renameOperationLogToOperationLogDate(dateTime);
            System.out.println("重命名表成功1");
            sqlMapper.renameOperationLogCloneToOperationLog();
            System.out.println("重命名表成功2");
            sqlMapper.removePartitioningOfOperationLog();
            System.out.println("删除分区成功");
            List<DeviceRegistry> list = deviceRegistryDao.findByIsRegistered(ConstantConfig.REGISTERED);
            if (list.size() == 0) {
                sqlMapper.createPartitioningOfOperationLog(1);
                System.out.println("重新分区成功");
            } else {
                sqlMapper.createPartitioningOfOperationLog(list.size());
                System.out.println("重新分区成功");
            }
        } catch (RuntimeException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//关键
            e.printStackTrace();
            if (refreshTimes > TRY_TIMES)
                return false;
            if (repairRefreshOperationLogTab()) {
                refreshTimes++;
                success = this.refreshOperationLogTab();
            }
        }
        return success;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean repairRefreshOperationLogTab() {
        boolean success = true;
        try {
            sqlMapper.dropOperationLogClone();
            sqlMapper.dropOperationLog();
            SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd");
            String dateTime = df.format(new Date());
            sqlMapper.dropOperationLogDate(dateTime);
            sqlMapper.restoreOperationLog();
        } catch (RuntimeException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//关键
            e.printStackTrace();
            if (repairTimes > TRY_TIMES)
                return false;
            repairTimes++;
            success = this.repairRefreshOperationLogTab();
        }
        return success;
    }

}
