package cn.com.payu.app.modules.service;

import cn.com.payu.app.config.QiakrConfig;
import cn.com.payu.app.modules.model.qiakr.req.*;
import cn.com.payu.app.modules.model.qiakr.resp.*;
import cn.com.payu.app.modules.utils.QiakrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QiakrService {

    @Autowired
    private QiakrConfig qiakrConfig;

    @Autowired
    private QiakrUtils qiakrUtils;

    public QiakrStockCuserCheckResp checkStockCuser(QiakrStockCuserCheckReq req) {
        QiakrStockCuserCheckResp resp = qiakrUtils.access("userCheck", req, QiakrStockCuserCheckResp.class);

        return resp;
    }

    public QiakrApplicationResp application(QiakrApplicationReq req) {
        QiakrApplicationResp resp = qiakrUtils.access("creditPush", req, QiakrApplicationResp.class);

        return resp;
    }

    public QiakrAuditResultResp getAuditResult(QiakrAuditResultReq req) {
        QiakrAuditResultResp resp = qiakrUtils.access("creditQuery", req, QiakrAuditResultResp.class);

        return resp;
    }

    public QiakrGetProductUrlResp getProductUrl(QiakrGetProductUrlReq req) {
        QiakrGetProductUrlResp resp = qiakrUtils.access("getLoanUrl", req, QiakrGetProductUrlResp.class);

        return resp;
    }

    public List<QiakrAgreementItemResp> getAgreements(QiakrAgreementReq req) {
        QiakrAgreementResp resp = qiakrUtils.access("agreementList", req, QiakrAgreementResp.class);

        return resp.getList();
    }

}
