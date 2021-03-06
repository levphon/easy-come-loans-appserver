package cn.com.payu.app.modules.utils;

import com.glsx.plat.common.utils.encryption.Encrypter;
import com.glsx.plat.common.utils.encryption.PBEEncrypter;
import org.apache.commons.lang.StringUtils;

import java.nio.charset.StandardCharsets;

/**
 * 对字符串解密与解密功能
 */
public final class EncryptUtil {

    //盐
    private static final byte[] SLAT_ARRAY = new byte[]{24, 17, 61, 78, 9, 45, 13, 55};

    //密钥种子
    private static final String PWD_ARRAY = "xinxinrong2021";

    /**
     * 对v值进行加密，加密后以base64编码格式输出，如果加密失败，则返回null。
     *
     * @param v :java.lang.String，需要进行加密的值
     * @return java.lang.String
     */
    public static String PBEEncrypt(String v) {
        try {
            return StringUtils.trim(Encrypter.encryptBASE64(PBEEncrypter.encrypt(v.getBytes(StandardCharsets.UTF_8), PWD_ARRAY, SLAT_ARRAY)));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 对v值进行解密，解密后以base64编码格式输出，如果解密失败，则返回null。
     *
     * @param v :java.lang.String，需要进行解密的值
     * @return java.lang.String
     */
    public static String PBEDecrypt(String v) {
        try {
            return new String(PBEEncrypter.decrypt(PBEEncrypter.decryptBASE64(v), PWD_ARRAY, SLAT_ARRAY));
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        String a = "18682185876";
        String t = PBEEncrypt(a);
        System.out.println(a);
        System.out.println(t);
        System.out.println(PBEDecrypt(t));

        System.out.println(PBEDecrypt("v3ZqKdE5d72AGFLXKbtgeA=="));
    }
}
