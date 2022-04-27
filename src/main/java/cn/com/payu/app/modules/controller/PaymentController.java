package cn.com.payu.app.modules.controller;

import cn.com.payu.app.common.exception.AppServerException;
import cn.com.payu.app.config.LeChargeConfig;
import cn.com.payu.app.modules.model.PrepayBO;
import cn.com.payu.app.modules.service.PaymentService;
import cn.com.payu.app.modules.service.UnifiedPayService;
import cn.com.payu.app.modules.utils.LeChargeMD5;
import com.alipay.api.AlipayApiException;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.glsx.plat.common.annotation.SysLog;
import com.glsx.plat.core.web.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/payment")
@Api(value = "支付接口", tags = {"支付接口"})
public class PaymentController {

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UnifiedPayService unifiedPayService;

    @Autowired
    private LeChargeConfig config;

    /**
     * 预支付接口
     *
     * @param prepayBO
     * @return
     */
    @SysLog
    @ApiOperation(value = "预支付接口")
    @PostMapping(value = "/prepay")
    public R prepay(@RequestBody @Validated PrepayBO prepayBO) {
        log.info("预支付参数：{}", prepayBO.toString());
        if (prepayBO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new AppServerException("支付金额有误，请联系客服！");
        }
        Object result = paymentService.prepay(prepayBO);
        return R.ok().data(result);
    }

    /**
     * 微信支付回调接口
     *
     * @return
     */
    @SysLog
    @PostMapping(value = "/callback/wxPay", produces = {"application/xml; charset=UTF-8"})
    @ResponseBody
    public String callbackWxPay(@RequestBody String xmlData) {
        log.info("================================================开始处理微信支付发送的异步通知");
        try {
            final WxPayOrderNotifyResult result = this.wxPayService.parseOrderNotifyResult(xmlData);

            /**
             *  系统内部业务，修改订单状态之类的,判断数据是否重复处理
             */
            unifiedPayService.updateWxPayOrder(result);

            //成功后回调微信信息
            return WxPayNotifyResponse.success("回调成功！");
        } catch (WxPayException e) {
            e.printStackTrace();
            return WxPayNotifyResponse.fail("回调有误!");
        }
    }

    /**
     * 乐充支付回调接口
     *
     * @return
     */
    @SysLog
    @PostMapping(value = "/callback/leCharge")
    @ResponseBody
    public String callbackLeCharge(HttpServletRequest request) {
        log.info("================================================开始处理乐充发送的异步通知");
        Base64 base64 = new Base64();
        String secretKey = config.getSecretKey();
        String sellerid = request.getParameter("sellerid") == null ? "" : request.getParameter("sellerid");//    是    string    商户请求流水号
        String agentcode = request.getParameter("agentcode") == null ? "" : request.getParameter("agentcode");//    是    string    商户编号
        String account_encode = request.getParameter("account") == null ? "" : request.getParameter("account");//    是    string    充值账号 bese64后urlcode处理
        String account = new String(base64.decode(account_encode), StandardCharsets.UTF_8);
        String value = request.getParameter("value") == null ? "" : request.getParameter("value");//    是    string    充值金额
        String payvalue = request.getParameter("payvalue") == null ? "" : request.getParameter("payvalue");//    是    string    支付金额
        String createtime = request.getParameter("createtime") == null ? "" : request.getParameter("createtime");//    是    string    订单请求时间
        String endtime = request.getParameter("endtime") == null ? "" : request.getParameter("endtime");//    是    string    订单完成时间
        String state = request.getParameter("state") == null ? "" : request.getParameter("state");//    是    string    订单状态
        String voucher = request.getParameter("voucher") == null ? "" : request.getParameter("voucher");//    是    string    凭证
        String remark_encode = request.getParameter("remark") == null ? "" : request.getParameter("remark");//    是    string    备注 bese64后urlcode处理
        String remark = new String(base64.decode(remark_encode), StandardCharsets.UTF_8);
        String time = request.getParameter("time") == null ? "" : request.getParameter("time");//    是    string    密码
        String sign = request.getParameter("sign") == null ? "" : request.getParameter("sign");//    否    string    签名md5(sellerid+agentcode+account_base64+value+payvalue+voucher+createtime+endtime+state+remark_base64+time+secretKey)
        LeChargeMD5 md5 = new LeChargeMD5();
        String checkSign = md5.calcMD5(sellerid + agentcode + account_encode + value + payvalue + voucher + createtime + endtime + state + remark_encode + time + secretKey);
        log.info("乐购签名 {}", sign);
        log.info("本地签名 {}", checkSign);
        if (sign.equalsIgnoreCase(checkSign)) {
            unifiedPayService.updateLeChargeOrder(sellerid, state);
            return "ok";
        } else {
            log.error("签名失败");
            return "fail";
        }
    }

    /**
     * 支付宝支付回调接口
     *
     * @return
     */
    @SysLog
    @PostMapping(value = "/callback/aliPay")
    @ResponseBody
    public String callbackAliPay(HttpServletRequest request) throws AlipayApiException {
        log.info("================================================开始处理支付宝支付发送的异步通知");
        String success = "success";
        String failure = "failure";
        //获取支付宝的请求信息
        Map<String, String> params = new HashMap<>();

        Map<String, String[]> requestParams = request.getParameterMap();
        // 将 Map<String,String[]> 转为 Map<String,String>
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        params.remove("sign_type");
        log.info("支付宝回调验签参数 {}", params);
        if (params.isEmpty()) {
            return "success";
        }

        // 验签
        boolean signVerified = unifiedPayService.verifyAlipaySign(params);
        String outTradeNo = params.get("out_trade_no");
        log.info("{} 验签结果：{}", outTradeNo, signVerified);
        // 验签通过
        if (signVerified) {
            // 更新订单信息
            String result = unifiedPayService.updateAlipayOrder(params);
            if ("success".equals(result)) {
                return success;
            }
        }
        return failure;
    }

}
