package cn.com.payu.app.modules.utils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * CreateRsaKey
 * 生成RSA公私钥-简化版
 *
 * @author zhenzhong.li
 * @date 18/1/4
 */
public class CreateKeys {
    public static final String KEY_ALGORITHM = "RSA";
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    public static final Base64.Decoder decoder = Base64.getDecoder();
    public static final Base64.Encoder encoder = Base64.getEncoder();

    /**
     * 获得公钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    private static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        //获得map中的公钥对象 转为key对象
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        //编码返回字符串
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 获得私钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    private static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        //获得map中的私钥对象 转为key对象
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        //编码返回字符串
        return encryptBASE64(key.getEncoded());
    }

    //编码返回字符串
    private static String encryptBASE64(byte[] key) throws Exception {
        return encoder.encodeToString(key);
    }

    //map对象中存放公私钥
    private static Map<String, Object> initKey() throws Exception {
        //获得对象 KeyPairGenerator 参数 RSA 1024个字节
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        //通过对象 KeyPairGenerator 获取对象KeyPair
        KeyPair keyPair = keyPairGen.generateKeyPair();
        //通过对象 KeyPair 获取RSA公私钥对象RSAPublicKey RSAPrivateKey
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //公私钥对象存入map中
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * 生成RSA
     *
     * @return
     */
    public static Map<String, String> getRSAKey() {
        Map<String, Object> keyMapObj;
        Map<String, String> keyMap = new HashMap<String, String>();
        try {
            keyMapObj = initKey();
            String publicKey = getPublicKey(keyMapObj);
            keyMap.put("pubKey", publicKey);
            String privateKey = getPrivateKey(keyMapObj);
            keyMap.put("priKey", privateKey);
        } catch (Exception e) {
            System.out.println("生成公私钥异常:" + e.getMessage());
            return null;
        }
        return keyMap;
    }

    /**
     * 生成AES秘钥
     *
     * @return
     * @throws Exception
     */
    public static String getAESKey() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128);//要生成多少位，只需要修改这里即可128, 192或256
            SecretKey sk = kg.generateKey();
            byte[] b = sk.getEncoded();
            return parseByte2HexStr(b);
        } catch (Exception e) {
            System.out.println("生成AESKEY异常:" + e.getMessage());
        }
        return "";
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    private static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {

        //RSAkey
        Map map = CreateKeys.getRSAKey();
        String pubKey = map.get("pubKey").toString();
        String priKey = map.get("priKey").toString();
        System.out.println(pubKey);
        System.out.println(priKey);

        //AESkey
        String aesKey = CreateKeys.getAESKey();
        System.out.println(aesKey);
    }
}
