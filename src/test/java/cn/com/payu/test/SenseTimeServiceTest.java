package cn.com.payu.test;

import cn.com.payu.Application;
import cn.com.payu.app.modules.model.sensetime.req.OcrIdcardStatelessReq;
import cn.com.payu.app.modules.model.sensetime.resp.OcrIdcardStatelessResp;
import cn.com.payu.app.modules.service.SenseTimeService;
import cn.com.payu.app.modules.utils.Base64Utils;
import cn.hutool.json.JSONUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SenseTimeServiceTest {

    @Autowired
    private SenseTimeService senseTimeService;

    @Test
    public void test() {
        String imgBase64 = Base64Utils.ImageToBase64ByLocal("F:\\data\\files\\国徽_20220121155326.png");
        OcrIdcardStatelessReq req = new OcrIdcardStatelessReq();
        req.setImageBase64(imgBase64);
        req.setSide("back");
        OcrIdcardStatelessResp resp = senseTimeService.ocrIdcardStateless(req, "", 1L);
        System.out.println(JSONUtil.toJsonStr(resp));
    }

}
