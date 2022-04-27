package cn.com.payu.test;


import cn.com.payu.app.modules.utils.DanmiHttpUtil;
import cn.com.payu.test.common.DanmiConfig;

import java.net.URLEncoder;

/**
 * 短信API发送
 *
 * @author JiangPengFei
 * @version $Id: javaHttpNewApiDemo, v 0.1 2019/1/23 11:42 JiangPengFei Exp $$
 */
public class SmsApiHttpSendTest {

    /**
     * 短信发送(验证码通知，会员营销)
     * 接口文档地址：http://www.danmi.com/developer.html#smsSend
     */
    public void execute() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("accountSid").append("=").append(DanmiConfig.ACCOUNT_SID);
        sb.append("&to").append("=").append("130xxxxxxxx");
        sb.append("&param").append("=").append(URLEncoder.encode("", "UTF-8"));
        sb.append("&templateid").append("=").append("1251");
//		sb.append("&smsContent").append("=").append( URLEncoder.encode("【旦米科技】您的验证码为123456，该验证码5分钟内有效。请勿泄漏于他人。","UTF-8"));
        String body = sb + DanmiHttpUtil.createCommonParam(DanmiConfig.ACCOUNT_SID, DanmiConfig.AUTH_TOKEN);
        String result = DanmiHttpUtil.post(DanmiConfig.BASE_URL, body);
        System.out.println(result);
    }

    public static void main(String[] args) {
        SmsApiHttpSendTest am = new SmsApiHttpSendTest();
        try {
            am.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
