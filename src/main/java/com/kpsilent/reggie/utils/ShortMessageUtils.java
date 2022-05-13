// This file is auto-generated, don't edit it. Thanks.
package com.kpsilent.reggie.utils;

import com.aliyun.tea.*;
import com.aliyun.dysmsapi20170525.*;
import com.aliyun.dysmsapi20170525.models.*;
import com.aliyun.teaopenapi.*;
import com.aliyun.teaopenapi.models.*;
import java.util.List;
public class ShortMessageUtils {

    /**
     * 使用AK&SK初始化账号Client
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.dysmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }
//    用户登录名称 lzs@1561597149688994.onaliyun.com
//    AccessKey ID LTAI5t7d4uJapsd8kkhTP41a
//    AccessKey Secret fGmcIa31Aj7duJRD1BjeZk2Kr16VTx
    public static void sendMsg(String phone, String code) throws Exception {
//        java.util.List<String> args = java.util.Arrays.asList(args_);
        com.aliyun.dysmsapi20170525.Client client = ShortMessageUtils.createClient("LTAI5t7d4uJapsd8kkhTP41a", "fGmcIa31Aj7duJRD1BjeZk2Kr16VTx");
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName("阿里云短信测试")
                .setTemplateCode("SMS_154950909")
                .setPhoneNumbers(phone)
                .setTemplateParam("{\"code\":\"" + code + "\"}");
        // 复制代码运行请自行打印 API 的返回值
        client.sendSms(sendSmsRequest);
    }
}
