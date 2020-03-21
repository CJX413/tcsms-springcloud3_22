package com.tcsms.business.Service.ReceiveServiceImp;


import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tcsms.business.Entity.OperationLog;
import com.tcsms.business.Entity.SmsParams;
import lombok.extern.log4j.Log4j2;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
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

    // 短信模板ID，需要在短信应用中申请
    @Value("${tx.sms.templateId}")
    private int templateId;

    // 签名
    @Value("${tx.sms.smsSign}")
    private String smsSign;

    @Value("${tx.sms.EffectiveTime}")
    private String smsEffectiveTime;

    @Autowired
    UserServiceImp userServiceImp;
    @Autowired
    RedisServiceImp redisServiceImp;

    /**
     * 指定模板 ID 单发短信
     *
     * @param phone
     */
    public void sendSmsVerifyCode(String phone) {
        boolean success = false;
        try {
            Random random = new Random(4);
            Integer verifyCode = random.nextInt(1000000);
            String[] params = {verifyCode.toString(), smsEffectiveTime};
            SmsSingleSender ssender = new SmsSingleSender(appId, appKey);
            SmsSingleSenderResult result = ssender.sendWithParam("86", phone,
                    templateId, params, smsSign, "", "");
            log.info(result);
            if (result.result == 0) {
                success = true;
            } else {
                success = false;
            }
        } catch (HTTPException e) {
            // HTTP 响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // JSON 解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络 IO 错误
            e.printStackTrace();
        }
    }

    public void multiSendSmsMessage(SmsParams smsParams) {
        boolean success = false;
        try {
            String[] params = {smsParams.getMessage()};
            SmsMultiSender msender = new SmsMultiSender(appId, appKey);
            SmsMultiSenderResult result = msender.sendWithParam("86", smsParams.getPhone(),
                    templateId, params, smsSign, "", "");
            log.info(result);
            if (result.result == 0) {
                success = true;
            } else {
                success = false;
            }
        } catch (HTTPException e) {
            // HTTP 响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // JSON 解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络 IO 错误
            e.printStackTrace();
        }
    }

    public void sendWarningToMonitor(String json) {
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        StringBuffer device = new StringBuffer();
        JsonElement jsonElement = jsonObject.get("data");
        if (!jsonElement.isJsonNull() && jsonElement.isJsonArray()) {
            jsonElement.getAsJsonArray().forEach(items -> {
                OperationLog operationLog = new Gson().fromJson(items, OperationLog.class);
                device.append("设备").append(operationLog.getDeviceId()).append(",驾驶员为")
                        .append(operationLog.getOperator()).append(",发出");
            });
        }
        String warningMessage = device.append(jsonObject.get("message").getAsString()).toString();
        String[] phone = userServiceImp.getPhoneOfMonitor();
        SmsParams smsParams = new SmsParams(phone, warningMessage);
        //multiSendSmsMessage(smsParams);
    }
}
