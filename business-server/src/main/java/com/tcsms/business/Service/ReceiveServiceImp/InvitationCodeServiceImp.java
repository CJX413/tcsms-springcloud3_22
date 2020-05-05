package com.tcsms.business.Service.ReceiveServiceImp;

import com.tcsms.business.Dao.InvitationCodeDao;
import com.tcsms.business.Entity.InvitationCode;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class InvitationCodeServiceImp {
    private final static int invitationCodeId = 1;
    @Autowired
    private InvitationCodeDao invitationCodeDao;

    public InvitationCode getInvitationCode() {
        return invitationCodeDao.findById(invitationCodeId).orElse(new InvitationCode(invitationCodeId, ""));
    }

    public void save(String invitationCode) throws RuntimeException {
        invitationCodeDao.save(new InvitationCode(invitationCodeId, invitationCode));
    }

    public boolean checkInvitationCode(String invitationCode) throws RuntimeException {
        Optional<InvitationCode> optional = invitationCodeDao.findById(invitationCodeId);
        if (optional.isPresent()) {
            log.info(optional.get().getCode() + "---" + invitationCode);
            if (optional.get().getCode().equals(invitationCode)) {
                return true;
            }
            return false;
        }
        return false;
    }
}
