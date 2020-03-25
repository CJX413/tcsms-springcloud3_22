package com.tcsms.business.Service.ReceiveServiceImp;

import com.tcsms.business.Dao.RoleApplyDao;
import com.tcsms.business.Entity.RoleApply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleApplyServiceImp {
    @Autowired
    private RoleApplyDao roleApplyDao;

    @Autowired
    private UserServiceImp userServiceImp;

    public RoleApplyDao getDao() {
        return roleApplyDao;
    }

    public void applyMonitor(String username) throws RuntimeException {
        if (isApplyRole(username)) {
            class ApplyMonitorRuntimeException extends RuntimeException {
                private ApplyMonitorRuntimeException() {
                    super("您已经申请过MONITOR角色了！");
                }
            }
            throw new ApplyMonitorRuntimeException();
        } else {
            RoleApply roleApply = new RoleApply();
            roleApply.setUsername(username);
            roleApply.setRole("MONITOR");
            roleApplyDao.save(roleApply);
        }
    }

    public void agreeApplyRole(RoleApply roleApply) throws RuntimeException{
        userServiceImp.changeRoleByUsername(roleApply.getUsername(), roleApply.getRole());
        roleApplyDao.deleteById(roleApply.getUsername());
    }

    public void refuseApplyRole(RoleApply roleApply) throws RuntimeException{
        roleApplyDao.deleteById(roleApply.getUsername());
    }

    public boolean isApplyRole(String username) {
        return roleApplyDao.findById(username).isPresent();
    }
}
