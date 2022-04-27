package cn.com.payu.app.modules.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * BairongSignature
 * 签名加密工具类-简化版
 *
 * @author zhenzhong.li
 * @date 18/1/4
 */
public class BairongSignature {

    private static final String charset = "utf-8";
    private static final String signTypeSHA1 = "SHA1withRSA";
    private static final String signTypeSHA256 = "SHA256withRSA";
    private static final String signTypeMD5 = "MD5withRSA";
    //AES_256_cbc pkcs7
    private static final String algorithm = "AES/CBC/PKCS5Padding";

    public static final java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
    public static final java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();


    /**
     * 加签
     *
     * @param signParams
     * @param privateKey
     * @return
     */
    public static String signMD5(Map<String, String> signParams, String privateKey) {
        return initSign(signTypeMD5, signParams, privateKey);
    }

    public static String signSHA1(Map<String, String> signParams, String privateKey) {
        return initSign(signTypeSHA1, signParams, privateKey);
    }

    public static String signSHA256(Map<String, String> signParams, String privateKey) {
        return initSign(signTypeSHA256, signParams, privateKey);
    }

    /**
     * 根据类型生成签名
     *
     * @param privateKey
     */
    private static String initSign(String signType, Map<String, String> signParams, String privateKey) {
        try {
            String content = getSignContent(signParams);
            PrivateKey priKey = getPrivateKeyFromPKCS8(privateKey);
            Signature signature = Signature.getInstance(signType);
            signature.initSign(priKey);
            if (isEmpty(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }
            byte[] signed = signature.sign();
            return new String(encoder.encode(signed));
        } catch (Exception e) {
            System.out.println("生成签名异常:" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 验证签名
     *
     * @param signParams
     * @param sign
     * @param publicKey
     * @return
     */
    public static boolean checkSignMD5(Map<String, String> signParams, String sign, String publicKey) {
        return rsa256CheckContent(signTypeMD5, signParams, sign, publicKey);
    }

    public static boolean checkSignSHA1(Map<String, String> signParams, String sign, String publicKey) {
        return rsa256CheckContent(signTypeSHA1, signParams, sign, publicKey);
    }

    public static boolean checkSignSHA256(Map<String, String> signParams, String sign, String publicKey) {
        return rsa256CheckContent(signTypeSHA256, signParams, sign, publicKey);
    }

    /**
     * 根据加签类型验证签名
     *
     * @param signParams
     * @param sign
     * @param publicKey
     * @return
     */
    private static boolean rsa256CheckContent(String signType, Map<String, String> signParams, String sign, String publicKey) {
        try {
            String content = getSignContent(signParams);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = decoder.decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            Signature signature = Signature.getInstance(signType);

            signature.initVerify(pubKey);
            signature.update(content.getBytes(charset));
            boolean bverify = signature.verify(decoder.decode(sign));
            return bverify;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * RSA-公钥加密过程
     *
     * @param params
     * @param pubKey
     * @return
     * @throws Exception
     */
    public static String encryptRSA(Map<String, String> params, String pubKey) throws Exception {
        if (isEmpty(pubKey)) {
            throw new Exception("加密公钥为空, 请设置");
        }
        String context = getSignContent(params);
        PublicKey publicKey = getPublicKeyFromX509(pubKey);
        Cipher cipher;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA");
            // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] output = cipher.doFinal(context.getBytes());
            return encoder.encodeToString(output);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
    }

    /**
     * RSA-私钥解密过程
     *
     * @param context
     * @param priKey
     * @return
     * @throws Exception
     */
    public static String decryptRSA(String context, String priKey) throws Exception {
        if (isEmpty(priKey)) {
            throw new Exception("解密私钥为空, 请设置");
        }
        PrivateKey privateKey = getPrivateKeyFromPKCS8(priKey);
        Cipher cipher;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] output = cipher.doFinal(decoder.decode(context));
            return new String(output);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("解密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        }
    }

    /**
     * 参数格式转换Map转String
     *
     * @param sortedParams
     * @return
     */
    public static String getSignContent(Map<String, String> sortedParams) {
        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<String>(sortedParams.keySet());
        Collections.sort(keys);
        int index = 0;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = sortedParams.get(key);
            if (isNotEmpty(key) && isNotEmpty(value)) {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
                index++;
            }
        }
        return content.toString();
    }


    /**
     * 私钥对象生成
     *
     * @param priKey
     * @return
     * @throws Exception
     */
    private static PrivateKey getPrivateKeyFromPKCS8(String priKey) throws Exception {
        PKCS8EncodedKeySpec priPKCS8;
        PrivateKey privateKey = null;
        if (isEmpty(priKey)) {
            return privateKey;
        }
        try {
            priPKCS8 = new PKCS8EncodedKeySpec(decoder.decode(priKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            privateKey = keyf.generatePrivate(priPKCS8);
        } catch (Exception e) {
            System.out.println("私钥解析错误:" + e.getMessage());
        }
        return privateKey;
    }

    /**
     * 公钥对象生成
     *
     * @param pubKey
     * @return
     * @throws Exception
     */
    private static PublicKey getPublicKeyFromX509(String pubKey) throws Exception {
        PublicKey publicKey = null;
        if (isEmpty(pubKey)) {
            return publicKey;
        }
        try {
            X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(decoder.decode(pubKey));
            KeyFactory keyFactory;
            keyFactory = KeyFactory.getInstance("RSA");
            // 取公钥匙对象
            publicKey = keyFactory.generatePublic(bobPubKeySpec);
        } catch (Exception e) {

        }
        return publicKey;
    }

    /**
     * AES加密
     *
     * @param base64Key
     * @param text
     * @return
     * @throws Exception
     */
    public static String encryptAES(String base64Key, String text) {
        try {
            byte[] key = parseHexStr2Byte(base64Key);
            SecretKeySpec sKeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, sKeySpec);
            byte[] encryptBytes = cipher.doFinal(text.getBytes(charset));
            return parseByte2HexStr(encryptBytes);
        } catch (Exception e) {
            System.out.println("AES加密错误:" + e.getMessage());
        }
        return null;
    }

    /**
     * AES解密
     *
     * @param base64Key
     * @param text
     * @return
     * @throws Exception
     */
    public static String decryptAES(String base64Key, String text) {
        try {
            byte[] key = parseHexStr2Byte(base64Key);
            SecretKeySpec sKeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec);
            byte[] decryptBytes = cipher.doFinal(parseHexStr2Byte(text));
            return new String(decryptBytes, "UTF-8");
        } catch (Exception e) {
            System.out.println("AES解密错误:" + e.getMessage());
        }
        return null;
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
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


    private static boolean isEmpty(String str) {
        return ((str == null) || (str.length() == 0));
    }

    private static boolean isNotEmpty(String str) {
        return ((str != null) && (str.length() > 0));
    }


    public static void main(String[] arg) {
        //--------生成RSA秘钥对--------
        System.out.println("********create RSA keys********");
        Map map = CreateKeys.getRSAKey();
        //公钥
        String pubKey = map.get("pubKey").toString();
        System.out.println("RSA pubKey:" + pubKey);
        //私钥
        String priKey = map.get("priKey").toString();
        System.out.println("RSA priKey:" + priKey);


        //--------RSA 加签--------
        System.out.println("********RSA create sign ********");
        //加签参数
        Map param = new HashMap();
        param.put("user", "123");
        param.put("time", "123431123");
        //生成签名字符串
        System.out.println("sign param :" + BairongSignature.getSignContent(param));
        String signStr = BairongSignature.signSHA256(param, priKey);
        System.out.println("sign str :" + signStr);
        param.put("sign", signStr);

        //--------RSA 验证--------
        System.out.println("********RSA check sign ********");
        String singStr1 = (String) param.get("sign");

        //获取验证参数,与加签一致
        Map param1 = new HashMap();
        param1.put("user", param.get("user"));
        param1.put("time", param.get("time"));
        System.out.println("check sign param :" + BairongSignature.getSignContent(param));
        boolean flag = BairongSignature.checkSignSHA256(param1, singStr1, pubKey);
        System.out.println("check sign flag:" + flag);


        //--------RSA 加解密--------
        System.out.println("********RSA encrypt/decrypt ********");
        Map param2 = new HashMap();
        param2.put("user", "123");
        param2.put("time", "123431123");
        param2.put("context", "ssdad212e12fdasda");
        System.out.println("rsa param:" + BairongSignature.getSignContent(param2));
        try {
            //加密
            String enStr = BairongSignature.encryptRSA(param2, pubKey);
            System.out.println("rsa encrypt:" + enStr);

            //解密
            String deStr = BairongSignature.decryptRSA(enStr, priKey);
            System.out.println("rsa decrypt:" + deStr);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //--------AES 加解密----------
        System.out.println("********AES encrypt/decrypt ********");
        //加密字符串
        String content = "hello world";
        //生成AES秘钥
        String aeskey = CreateKeys.getAESKey();
        System.out.println("AES KEY:" + aeskey);

        //发送者加密
        System.out.println("encrypt param:" + content);
        String encode = BairongSignature.encryptAES(aeskey, content);
        System.out.println("encrypt :" + encode);

        //接收者解密
        String decoded = BairongSignature.decryptAES(aeskey, encode);
        System.out.println("decrypt :" + decoded);
    }
}
