package cn.com.payu.test;

import cn.com.payu.Application;
import cn.com.payu.app.common.enums.AliyunSMSTemplate;
import cn.com.payu.app.common.enums.OrderChannel;
import cn.com.payu.app.modules.entity.ChannelDownload;
import cn.com.payu.app.modules.entity.Order;
import cn.com.payu.app.modules.entity.UserVip;
import cn.com.payu.app.modules.mapper.ChannelDownloadMapper;
import cn.com.payu.app.modules.mapper.OrderMapper;
import cn.com.payu.app.modules.model.AppUser;
import cn.com.payu.app.modules.model.LeChargeSubmitOrderModel;
import cn.com.payu.app.modules.model.UnifiedPayModel;
import cn.com.payu.app.modules.model.lecharge.resp.LeChargeGetBalanceResponse;
import cn.com.payu.app.modules.model.lecharge.resp.LeChargeQueryOrderResponse;
import cn.com.payu.app.modules.model.lecharge.resp.LeChargeSubmitOrderResponse;
import cn.com.payu.app.modules.service.*;
import cn.com.payu.app.modules.utils.AppContextHolder;
import cn.com.payu.app.modules.utils.EncryptUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glsx.plat.common.utils.SnowFlake;
import com.glsx.plat.sms.utils.AliyunSmsUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private AliyunSmsUtils aliyunSmsUtils;

    @Autowired
    private UnifiedPayService unifiedPayService;

    @Autowired
    private VipService vipService;

    @Autowired
    private CouponService couponService;

    @Resource
    private OrderMapper orderMapper;

    //读取json文件
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8);
            int ch = 0;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Before
    public void before() {
        AppUser user = new AppUser();
        user.setUnionId("717341");
        user.setUsername("Jack");
        user.setAvatar("http://img.youdiany.com/group1/M00/00/00/rBdsOGAaGf6AOTRKAADF0hP_Hrc782.png");
        AppContextHolder.setUser(user);
    }

    @Test
    public void testAddBank() {
        String str = readJsonFile("G:\\workspace\\pomelo-fate\\pomelo-fate-appserver\\src\\test\\java\\bank.json");
        JSONArray banks = JSON.parseArray(str);//构建JSONArray数组
        System.out.println(banks.size());

        banks.forEach(obj -> {
            JSONObject jo = (JSONObject) obj;
            String code = jo.getString("code");
            String name = jo.getString("name");

            System.out.println("insert into base_bank(code, name) values ('" + code + "','" + name + "');");
        });
    }

    @Test
    public void testSms() {
//        smsService.sendCode("18682185876");

        aliyunSmsUtils.sendSms("18682185876", AliyunSMSTemplate.LOGIN_CONFIRM.getType(), "{\"code\":\"" + 123456 + "\"}");
    }

    @Test
    public void testPay() {
        UnifiedPayModel model = UnifiedPayModel.builder()
                .type(OrderChannel.WECHAT_PAY.getType())
                .tradeNo(SnowFlake.nextSerialNumber())
                .subject("充值")
                .totalAmount(new BigDecimal("0.01"))
                .build();

        Object result = unifiedPayService.unifiedPrePay(model);
        System.out.println(result);
    }


    @Test
    public void testUser() {
        AppUser user = userService.getAppUserByUserId(2L);
        System.out.println(user);
    }


    @Test
    public void testVip() {
        Order order = orderMapper.selectById(68L);
        vipService.subscribe(order);

        UserVip userVip = vipService.getUserVipInfo(6L);
        userVip.setStatus(1);
        boolean vipFlag = vipService.isValidVip(userVip);
        System.out.println(vipFlag);
    }

    @Test
    public void testRakeBack() {
//        orderService.completeCharge(rakeBackBO.getOrderNo());
//        rewardService.rakeBack(AppContextHolder.getUserId(), rakeBackBO.getAmount());
    }


    @Autowired
    private LeChargeService leChargeService;

    @Test
    public void testLeCharge() {
        LeChargeGetBalanceResponse response1 = leChargeService.getBalance();

        String sellerId = SnowFlake.nextSerialNumber();
        LeChargeSubmitOrderModel request = new LeChargeSubmitOrderModel();
        request.setSellerId(sellerId);
        request.setAccount("18682185876");
        request.setCode("CESHI");
        request.setNum(String.valueOf(1));
        request.setValue(String.valueOf(0));
        request.setRemark("测试充值");
        LeChargeSubmitOrderResponse response2 = leChargeService.submitOrder(request);

        LeChargeQueryOrderResponse response3 = leChargeService.queryOrder(sellerId);

        System.out.println(JSONUtil.toJsonStr(response1));
        System.out.println(JSONUtil.toJsonStr(response2));
        System.out.println(JSONUtil.toJsonStr(response3));
    }

    @Test
    public void testCoupon() {
        couponService.assignCoupons(2L, 8L, 12);
    }

    @Autowired
    private OrderService orderService;

    @Test
    public void testLeCharge2() {
        orderService.completeCharge("495727109189206016", "");
    }

    @Resource
    private ChannelDownloadMapper channelDownloadMapper;

    @Test
    public void testDecrypt() {
        List<ChannelDownload> downloadList = channelDownloadMapper.selectEncryptAccount();
        System.out.println(downloadList.size());
        downloadList.forEach(cd -> {
            String decryptAccount = EncryptUtil.PBEDecrypt(cd.getAccount());
            System.out.println(cd.getAccount() + ">>>" + decryptAccount);
            channelDownloadMapper.updateDecryptAccount(cd.getId(), decryptAccount);
        });
    }
}