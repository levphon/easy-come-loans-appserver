package cn.com.payu.app.modules.service;

import cn.com.payu.app.common.enums.GoodsType;
import cn.com.payu.app.common.enums.OrderChannel;
import cn.com.payu.app.common.enums.OrderIEType;
import cn.com.payu.app.common.enums.OrderStatus;
import cn.com.payu.app.config.QiakrConfig;
import cn.com.payu.app.modules.entity.*;
import cn.com.payu.app.modules.mapper.*;
import cn.com.payu.app.modules.model.qiakr.req.*;
import cn.com.payu.app.modules.model.qiakr.resp.QiakrApplicationResp;
import cn.com.payu.app.modules.model.qiakr.resp.QiakrAuditResultResp;
import cn.com.payu.app.modules.model.qiakr.resp.QiakrGetProductUrlResp;
import com.glsx.plat.common.utils.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class QiakrBizService {

    @Resource
    private OrderApplicationMapper applicationMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserProfileMapper userProfileMapper;

    @Resource
    private CustUserContactMapper custUserContactMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OcrIdcardMapper ocrIdcardMapper;

    @Resource
    private SensetimeLivenessMapper sensetimeLivenessMapper;

    @Autowired
    private QiakrService qiakrService;

    @Autowired
    private QiakrConfig qiakrConfig;

    @Async
    @Transactional
    public void application(Long userId) {
        User user = userMapper.selectById(userId);

        UserProfile userProfile = userProfileMapper.selectByUserId(userId);

        String orderNo = SnowFlake.nextSerialNumber();

        Order order = new Order();
        order.setCuserId(userId);
        order.setOrderNo(orderNo);
        order.setPayChannel(OrderChannel.QIAKR.getType());
        order.setOrderType(GoodsType.YOUHUI_BUY.getType());
        order.setIeType(OrderIEType.EXPENSE.getType());
        order.setAmount(BigDecimal.ZERO);
        order.setStatus(OrderStatus.PAYING.getCode());
        order.setCreatedDate(new Date());
        orderMapper.insertUseGeneratedKeys(order);

        OrderApplication application = new OrderApplication();
        application.setCuserId(userId);
        application.setOrderId(order.getId());
        application.setStatus(0);
        application.setCreatedDate(new Date());
        applicationMapper.insert(application);

        QiakrApplicationBasicInfo basicInfo = new QiakrApplicationBasicInfo();
        basicInfo.setHouseProvince(userProfile.getProvince());
        basicInfo.setHouseCity(userProfile.getCity());
        basicInfo.setHouseAddress(userProfile.getAddress());
        basicInfo.setCompanyName(userProfile.getCompany());
        basicInfo.setCompanyProvince(userProfile.getCompanyProvince());
        basicInfo.setCompanyCity(userProfile.getCompanyCity());
        basicInfo.setUnitAddress(userProfile.getCompanyAddress());
        basicInfo.setDegree(userProfile.getEducationCode());
        basicInfo.setMarriage(userProfile.getMaritalStatusCode());

        String idcard = userProfile.getIdentityNo();


        QiakrApplicationIdInfo idInfo = new QiakrApplicationIdInfo();
        idInfo.setCid(idcard);
        idInfo.setName(userProfile.getRealName());
        idInfo.setCheckTime(System.currentTimeMillis());
        QiakrApplicationIdCardOcr idCardOcr = new QiakrApplicationIdCardOcr();

        OcrIdcard ocrIdcard = ocrIdcardMapper.selectByUserId(userId);
        if (ocrIdcard != null) {
            idInfo.setGender("男".equals(ocrIdcard.getGender()) ? 1 : 0);
            idInfo.setBirthday(ocrIdcard.getBirthday());

            //idInfo.setAddress(ocrIdcard.getAddress());
            //idInfo.setIssuedBy(ocrIdcard.getIssuedBy());
            //idInfo.setValidDate(ocrIdcard.getValidDate());
            //idInfo.setNation(ocrIdcard.getNation());

            idInfo.setAddress("--");
            idInfo.setIssuedBy("--");
            idInfo.setValidDate("--");
            idInfo.setNation("--");

            idCardOcr.setFrontPhotoUrl(ocrIdcard.getFrontUrl());
            idCardOcr.setBackPhotoUrl(ocrIdcard.getBackUrl());
        }

        SensetimeLiveness liveness = sensetimeLivenessMapper.selectLastLivenessByUserId(userId);
        if (liveness != null) {
            idCardOcr.setFaceUrl(liveness.getLivenessFaceImage());

            idInfo.setCheckTime(liveness.getCreatedDate().getTime());
        }

        QiakrApplicationContactInfo contact = new QiakrApplicationContactInfo();
        List<CustUserContact> contactList = custUserContactMapper.selectByUserId(userId);
        if (CollectionUtils.isNotEmpty(contactList)) {
            contact.setFirstName(contactList.get(0).getName());
            contact.setFirstPhone(contactList.get(0).getPhone());
            contact.setFirstRelation(contactList.get(0).getRelationCode());

            if (contactList.size() > 1) {
                contact.setSecondName(contactList.get(1).getName());
                contact.setSecondPhone(contactList.get(1).getPhone());
                contact.setSecondRelation(contactList.get(1).getRelationCode());
            }
        }

        QiakrApplicationBankCardInfo bankCardInfo = null;
        QiakrApplicationExtendInfo extendInfo = null;

        QiakrApplicationReq applicationReq = new QiakrApplicationReq();
        applicationReq.setUid(userProfile.getUserId());
        applicationReq.setAppId(qiakrConfig.getAppId());
        applicationReq.setOrderId(application.getId());
        applicationReq.setRegisterPhone(user.getAccount());
        applicationReq.setRegisterTime(user.getCreatedDate().getTime());

//        applicationReq.setLoanAmount(0);
//        applicationReq.setPeriod(0);
//        applicationReq.setPeriodUnit(0);
//        applicationReq.setApplyDate(System.currentTimeMillis());

        applicationReq.setBasicInfo(basicInfo);
        applicationReq.setIdInfo(idInfo);
        applicationReq.setContact(contact);
        applicationReq.setBankCardInfo(bankCardInfo);
        applicationReq.setExtendInfo(extendInfo);
        applicationReq.setIdCardOcr(idCardOcr);
        QiakrApplicationResp resp = qiakrService.application(applicationReq);
        if (resp != null && resp.getStatus() != null) {
            applicationMapper.updateStatusById(application.getId(), resp.getStatus());
            if (0 == resp.getStatus() || "0".equals(String.valueOf(resp.getStatus()))) {

            } else {

            }
        }
    }

    public QiakrAuditResultResp getResult(Long userId) {
        OrderApplication application = applicationMapper.selectByUserId(userId);
        if (application != null) {
            QiakrAuditResultReq auditResultReq = new QiakrAuditResultReq();
            auditResultReq.setOrderId(application.getId());
            QiakrAuditResultResp resp = qiakrService.getAuditResult(auditResultReq);
            return resp;
        }
        return null;
    }

    public String getProductUrl(Long userId) {
        OrderApplication application = applicationMapper.selectByUserId(userId);
        if (application != null) {
            QiakrGetProductUrlReq getProductUrlReq = new QiakrGetProductUrlReq();
            getProductUrlReq.setOrderId(application.getId());
            QiakrGetProductUrlResp resp = qiakrService.getProductUrl(getProductUrlReq);
            return resp.getUrl();
        }
        return null;
    }

}
