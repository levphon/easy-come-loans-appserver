package cn.com.payu.app.modules.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.client.ClientProtocolException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;

@Slf4j
public class GenerateString {

    public static final String id = "ac318aa00aea4bf68aed12914a1cf49d";
    public static final String secret = "2c435a89f05f489c86ea63874064620f";

    private static final String HASH_ALGORITHM = "HmacSHA256";
    static String timestamp = Long.toString(System.currentTimeMillis());
    static String nonce = RandomStringUtils.randomAlphanumeric(16);

    public static String genOriString(String api_key) {

        ArrayList<String> beforesort = new ArrayList<String>();
        beforesort.add(api_key);
        beforesort.add(timestamp);
        beforesort.add(nonce);

        Collections.sort(beforesort, new SpellComparator());
        StringBuffer aftersort = new StringBuffer();
        for (int i = 0; i < beforesort.size(); i++) {
            aftersort.append(beforesort.get(i));
        }

        String OriString = aftersort.toString();
        return OriString;
    }

    public static String genEncryptString(String genOriString, String api_secret) throws SignatureException {
        try {
            Key sk = new SecretKeySpec(api_secret.getBytes(), HASH_ALGORITHM);
            Mac mac = Mac.getInstance(sk.getAlgorithm());
            mac.init(sk);
            final byte[] hmac = mac.doFinal(genOriString.getBytes());
            StringBuilder sb = new StringBuilder(hmac.length * 2);

            @SuppressWarnings("resource")
            Formatter formatter = new Formatter(sb);
            for (byte b : hmac) {
                formatter.format("%02x", b);
            }
            String EncryptedString = sb.toString();
            return EncryptedString;
        } catch (NoSuchAlgorithmException e1) {
            throw new SignatureException("error building signature, no such algorithm in device " + HASH_ALGORITHM);
        } catch (InvalidKeyException e) {
            throw new SignatureException("error building signature, invalid key " + HASH_ALGORITHM);
        }
    }

    public static String genHeaderParam(String api_key, String api_secret) throws SignatureException {

        String GenOriString = genOriString(api_key);
        String EncryptedString = genEncryptString(GenOriString, api_secret);

        String HeaderParam = "key=" + api_key
                + ",timestamp=" + timestamp
                + ",nonce=" + nonce
                + ",signature=" + EncryptedString;

        return HeaderParam;
    }

    public static final String POST_URL = "https://v2-auth-api.visioncloudapi.com/v2/h5/liveness/auth_token";

    public static void doRequest() throws ClientProtocolException, IOException, SignatureException {
        //设置请求参数
        JSONObject jsonObject = JSONUtil.createObj();
        jsonObject.set("redirect_url", "https://www.baidu.com");
        jsonObject.set("is_show_default_result_page", "true");
        jsonObject.set("liveness_mode", "interactive");
        jsonObject.set("biz_no", "xxxxx");
        jsonObject.set("biz_extra_data", "xxxxx");
        jsonObject.set("web_title", "xxxxx");
        jsonObject.set("return_face_image", "true");
        jsonObject.set("return_image", "true");

        //发送post请求
        String postResult = HttpRequest.post(POST_URL)
                //设置请求头
                .header("Content-Type", "application/json")
                //这两个请求头是项目需要加的，可以省略
                .header("Authorization", genHeaderParam(id, secret))
                //传输参数
                .body(jsonObject.toString())
                .execute()
                .body();
        log.info("post结果打印 {}", postResult);
    }

    public static void main(String[] args) throws IOException, SignatureException {
        doRequest();
    }

}
