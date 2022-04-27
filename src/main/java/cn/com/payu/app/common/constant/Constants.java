package cn.com.payu.app.common.constant;

import com.glsx.plat.core.constant.BasicConstants;

/**
 * 〈一句话功能简述〉<br>
 * 公共常量类
 *
 * @author liuyf
 * @since 1.0.0
 */
public final class Constants extends BasicConstants {

    public final static String SERVER_NAME = "enjoy-spend";

    public final static String APP_SERVER_NAME = "enjoy-spend-appserver";

    public final static String MNG_SERVER_NAME = "enjoy-spend-mngserver";

    public final static int IS_ROOT_DEPARTMENT = 1;

    public final static int IS_NOT_ROOT_DEPARTMENT = 0;

    /**
     * 短信验证码前缀key
     */
    public static final String SMS_VERIFY_CODE_PREFIX = SERVER_NAME + ".sms.verifyCode.";
    /**
     * 短信验证码过时时间秒数
     */
    public static final long SMS_VERIFY_CODE_TIMEOUT = 360;
    /**
     * 短信验证码重复发送时间秒数
     */
    public static final long SMS_VERIFY_CODE_REPEAT_TIMEOUT = 60;

}
