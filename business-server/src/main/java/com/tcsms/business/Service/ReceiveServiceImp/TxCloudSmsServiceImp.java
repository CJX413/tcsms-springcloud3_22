package com.tcsms.business.Service.ReceiveServiceImp;


import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tcsms.business.Entity.OperationLog;
import com.tcsms.business.Entity.SmsParams;
import com.tcsms.business.Exception.CustomizeException;
import com.tcsms.business.JSON.ResultJSON;
import com.tcsms.business.Utils.JsonHelper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;


@Service
@Log4j2
@PropertySource("classpath:application.properties")
public class TxCloudSmsServiceImp {
    // 短信应用 SDK AppID
    @Value("${tx.sms.appId}")
    private int appId; // 1400开头

    // 短信应用SDK AppKey
    @Value("${tx.sms.appKey}")
    private String appKey;

    // 签名
    @Value("${tx.sms.smsSign}")
    private String smsSign;

    @Value("${tx.sms.EffectiveTime}")
    private String smsEffectiveTime;

    @Autowired
    UserServiceImp userServiceImp;
    @Autowired
    UserInfoServiceImp userInfoServiceImp;
    @Autowired
    RedisServiceImp redisServiceImp;
    // 注册短信模板ID，需要在短信应用中申请
    public static final int REGISTER_TEMPLATE_ID = 578632;
    // 修改短信模板ID，需要在短信应用中申请
    public static final int RESET_PWD_TEMPLATE_ID = 582540;
    public static final int WARNING_TEMPLATE_ID = 582540;

    /**
     * 指定模板 ID 单发短信
     *
     * @param phone
     */
    public ResultJSON sendSmsVerifyCode(String phone, Integer templateId) {
        try {
            Random random = new Random();
            String verifyCode = String.valueOf(random.nextInt(1000000));
            String[] params = {verifyCode, smsEffectiveTime};
            SmsSingleSender ssender = new SmsSingleSender(appId, appKey);
            SmsSingleSenderResult result = ssender.sendWithParam("86", phone,
                    templateId, params, smsSign, "", "");
            log.info(result);
            if (result.result == 0) {
                int expireSecond = Integer.valueOf(smsEffectiveTime) * 60;
                redisServiceImp.setVerifyCode(phone, verifyCode, expireSecond);
                return new ResultJSON(200, true, "发送验证码成功！", null);
            } else {
                return new ResultJSON(200, false, JsonHelper.replaceIllegalChar(result.errMsg), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJSON(200, false, JsonHelper.replaceIllegalChar(e.getMessage()), null);
        }
    }


    public void multiSendSmsMessage(SmsParams smsParams) throws Exception {
        SmsMultiSender msender = new SmsMultiSender(appId, appKey);
        SmsMultiSenderResult result = msender.sendWithParam("86", smsParams.getPhone(),
                WARNING_TEMPLATE_ID, smsParams.getParams(), smsSign, "", "");
        log.info(result);
        if (result.result != 0) {
            throw new CustomizeException(result.errMsg);
        }
    }

    public void sendWarning(String json) throws Exception{
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        StringBuffer message = new StringBuffer();
        JsonElement jsonElement = jsonObject.get("data");
        List<String> phoneList = userServiceImp.getPhoneOfMonitor();
        String adminsPhone = userServiceImp.getAdmin().getUserInfo().getPhoneNumber();
        phoneList.add(adminsPhone);
        if (!jsonElement.isJsonNull() && jsonElement.isJsonArray()) {
            jsonElement.getAsJsonArray().forEach(items -> {
                OperationLog operationLog = new Gson().fromJson(items, OperationLog.class);
                message.append("设备").append(operationLog.getDeviceId()).append(",驾驶员姓名为")
                        .append(operationLog.getOperator())
                        .append(",驾驶员工号为").append(operationLog.getWorkerId());
                String phoneNumber = userInfoServiceImp.getDao()
                        .findByWorkerId(operationLog.getWorkerId()).getPhoneNumber();
                phoneList.add(phoneNumber);
            });
        }
        String[] params = {message.append(",发出").append(jsonObject.get("message").getAsString()).toString()};
        String[] phone = new String[phoneList.size()];
        SmsParams smsParams = new SmsParams(phoneList.toArray(phone), params);
        log.info(smsParams.getParams());
        log.info(smsParams.getPhone());
        //multiSendSmsMessage(smsParams);
    }
}
