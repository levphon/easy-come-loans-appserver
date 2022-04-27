package cn.com.payu.test;

import cn.com.payu.Application;
import cn.com.payu.app.config.QiakrConfig;
import cn.com.payu.app.modules.entity.User;
import cn.com.payu.app.modules.entity.UserProfile;
import cn.com.payu.app.modules.model.qiakr.req.*;
import cn.com.payu.app.modules.model.qiakr.resp.QiakrApplicationResp;
import cn.com.payu.app.modules.model.qiakr.resp.QiakrAuditResultResp;
import cn.com.payu.app.modules.model.qiakr.resp.QiakrGetProductUrlResp;
import cn.com.payu.app.modules.model.qiakr.resp.QiakrStockCuserCheckResp;
import cn.com.payu.app.modules.service.QiakrService;
import cn.com.payu.app.modules.service.UserService;
import cn.com.payu.app.modules.utils.idcardUtil.IdcardInfoExtractor;
import cn.hutool.json.JSONUtil;
import com.glsx.plat.common.utils.DateUtils;
import com.glsx.plat.common.utils.SnowFlake;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QiakrServiceTest {

    @Autowired
    private QiakrService qiakrService;

    @Autowired
    private QiakrConfig qiakrConfig;

    @Autowired
    private UserService userService;

    @Test
    public void testStockCuserCheck() {
        QiakrStockCuserCheckReq checkReq = new QiakrStockCuserCheckReq();
        checkReq.setName("刘子阳");
        checkReq.setMd5("220881199611281512");
        checkReq.setMobileMd5("18243608060");
        QiakrStockCuserCheckResp checkResp = qiakrService.checkStockCuser(checkReq);
        System.out.println(checkResp);
    }

    @Test
    public void testApplication() {

        Long userId = 37L;
        User user = userService.getById(userId);
        UserProfile userProfile = userService.getUserProfile(userId);

        String appId = qiakrConfig.getAppId();
        Long orderNo = SnowFlake.nextId();
        System.out.println(orderNo);

        Integer loanAmount = 0;
        Integer period = 0;
        Integer periodUnit = 0;

        QiakrApplicationBasicInfo basicInfo = new QiakrApplicationBasicInfo();
        basicInfo.setHouseProvince("广东省");
        basicInfo.setHouseCity("佛山市");
        basicInfo.setHouseAddress("南海区纽约1号");
        basicInfo.setCompanyName(userProfile.getCompany());
        basicInfo.setCompanyProvince("广东省");
        basicInfo.setCompanyCity("佛山市");
        basicInfo.setUnitAddress(userProfile.getCompanyAddress());
        basicInfo.setDegree("30003");
        basicInfo.setMarriage("50001");

        String idcard = userProfile.getIdentityNo();
        IdcardInfoExtractor ie = new IdcardInfoExtractor(idcard);

        QiakrApplicationIdInfo idInfo = new QiakrApplicationIdInfo();
        idInfo.setCid(idcard);
        idInfo.setName(userProfile.getRealName());
        idInfo.setCheckTime(System.currentTimeMillis());
        idInfo.setGender(ie.getGender());
        idInfo.setAddress("陕西省平利县长安镇中坝村一组");
        idInfo.setIssuedBy("平利县公安局");
        idInfo.setValidDate("20060426-20260426");
        idInfo.setNation("汉");
        idInfo.setBirthday(DateUtils.format(ie.getBirthday(), "yyyyMMdd"));

        QiakrApplicationContactInfo contact = new QiakrApplicationContactInfo();
        contact.setFirstName("张三");
        contact.setFirstPhone("13800138001");
        contact.setFirstRelation("90001");
        contact.setSecondName("李四");
        contact.setSecondPhone("13800138002");
        contact.setSecondRelation("90003");

        QiakrApplicationBankCardInfo bankCardInfo = null;
        QiakrApplicationExtendInfo extendInfo = null;

        QiakrApplicationIdCardOcr idCardOcr = new QiakrApplicationIdCardOcr();
        idCardOcr.setFrontPhotoUrl("https://kr.zutuanla.com/file/mm/20211129/qzn5z4vo4ct.jpg");
        idCardOcr.setBackPhotoUrl("https://kr.zutuanla.com/file/mm/20211129/npb3j3gupyw.jpg");
        idCardOcr.setFaceUrl("https://kr.zutuanla.com/file/mm/20210426/bs1vgtrttmd.jpg");

        QiakrApplicationReq applicationReq = new QiakrApplicationReq();
        applicationReq.setUid(userProfile.getUserId());
        applicationReq.setAppId(appId);
        applicationReq.setOrderId(orderNo);
        applicationReq.setRegisterPhone(user.getAccount());
        applicationReq.setRegisterTime(user.getCreatedDate().getTime());

//        applicationReq.setLoanAmount();
//        applicationReq.setPeriod();
//        applicationReq.setPeriodUnit();
//        applicationReq.setApplyDate(System.currentTimeMillis());

        applicationReq.setBasicInfo(basicInfo);
        applicationReq.setIdInfo(idInfo);
        applicationReq.setContact(contact);
        applicationReq.setBankCardInfo(bankCardInfo);
        applicationReq.setExtendInfo(extendInfo);
        applicationReq.setIdCardOcr(idCardOcr);
        QiakrApplicationResp resp = qiakrService.application(applicationReq);
        System.out.println(JSONUtil.toJsonStr(resp));
    }

    @Test
    public void testAuditResult() {
        QiakrAuditResultReq auditResultReq = new QiakrAuditResultReq();
        auditResultReq.setOrderId(471438609082273792L);
        QiakrAuditResultResp resp = qiakrService.getAuditResult(auditResultReq);
        System.out.println(JSONUtil.toJsonStr(resp));
    }

    @Test
    public void testGetProductUrl() {
        QiakrGetProductUrlReq getProductUrlReq = new QiakrGetProductUrlReq();
        getProductUrlReq.setOrderId(471438609082273792L);
        QiakrGetProductUrlResp resp = qiakrService.getProductUrl(getProductUrlReq);
        System.out.println(JSONUtil.toJsonStr(resp));
    }

}
