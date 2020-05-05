package com.tcsms.business.Service.ReceiveServiceImp;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.tcsms.business.Dao.WarningDetailDao;
import com.tcsms.business.Dao.WarningLogDao;
import com.tcsms.business.Entity.WarningCount;
import com.tcsms.business.Entity.WarningDetail;
import com.tcsms.business.Entity.WarningLog;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class WarningLogServiceImp {
    @Autowired
    private WarningLogDao warningLogDao;
    @Autowired
    private WarningDetailDao warningDetailDao;

    public WarningDetailDao getWarningDetailDao() {
        return warningDetailDao;
    }

    public WarningLogDao getWarningLogDao() {
        return warningLogDao;
    }

    public WarningLog save(WarningLog warningLog, List<WarningDetail> list) {
        list.forEach(warningDetail -> {
            warningDetail.setWarningLog(warningLog);
        });
        warningLog.setWarningDetails(list);
        return warningLogDao.save(warningLog);
    }

    /**
     * 分类并获取WarningLog和warningDetail
     * @param deviceId
     * @param date
     * @return
     * @throws RuntimeException
     */
//    public JsonArray getWarningLogGroupByCodeByDeviceIdAndDate(String deviceId, String date) throws RuntimeException {
//        JsonArray jsonArray = new JsonArray();
//        HashMap<Integer, JsonArray> map = new HashMap<>();
//        Gson gson = new Gson();
//        Optional.of(warningLogDao.findByDeviceIdAndTime(deviceId, date)).ifPresent(warningLogs -> {
//            warningLogs.forEach(warningLog -> {
//                JsonArray value = map.getOrDefault(warningLog.getCode(), null);
//                if (value == null) {
//                    JsonArray array = new JsonArray();
//                    array.add(gson.fromJson(warningLog.toString(), JsonElement.class));
//                    map.put(warningLog.getCode(), array);
//                } else {
//                    value.add(gson.fromJson(warningLog.toString(), JsonElement.class));
//                    map.put(warningLog.getCode(), value);
//                }
//            });
//        });
//        map.forEach((key, value) -> {
//            jsonArray.add(value);
//        });
//        return jsonArray;
//    }

    /**
     * 分类获取警报的名称和个数
     * @param deviceId
     * @param date
     * @return
     */
    public JsonArray countWarningLogByDeviceIdAndDate(String deviceId, String date) {
        List<Object[]> list = warningLogDao.countByCodeAndTime(deviceId, date);
        Gson gson = new Gson();
        JsonArray jsonArray = new JsonArray();
        list.forEach(objects -> {
            WarningCount warningCount = new WarningCount(objects[0].toString(), Integer.parseInt(objects[1].toString()));
            jsonArray.add(gson.fromJson(warningCount.toString(), JsonElement.class));
        });
        return jsonArray;
    }

}
