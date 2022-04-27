package cn.com.payu.test;

import cn.com.payu.app.modules.utils.LeChargeMD5;
import cn.hutool.json.JSONUtil;
import com.glsx.plat.common.utils.DateUtils;
import com.glsx.plat.common.utils.HttpUtils;
import com.google.common.collect.Maps;

import java.util.Date;
import java.util.Map;

public class LeChargeTest {

    public static void main(String[] args) {
        String balanceUrl = "http://121.196.177.34:1012/api/md5/getbalance";
        String orderUrl = "http://121.196.177.34:1012/api/md5/queryorder";

        String agentcode = "202108311558";
        String time = DateUtils.formatSerial(new Date());
        String secretKey = "";
        String sign = new LeChargeMD5().calcMD5(agentcode + time + secretKey);

        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("agentcode", agentcode);
        paramMap.put("time", time);
        paramMap.put("sign", sign.toLowerCase());
        System.out.println(JSONUtil.toJsonStr(paramMap));

        String result = HttpUtils.post(balanceUrl, paramMap);
        System.out.println(result);
    }

}
