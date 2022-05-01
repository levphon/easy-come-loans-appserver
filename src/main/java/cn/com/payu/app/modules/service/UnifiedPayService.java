package cn.com.payu.app.modules.service;

import cn.com.payu.app.common.enums.OrderChannel;
import cn.com.payu.app.common.exception.AppServerException;
import cn.com.payu.app.config.AlipayConfig;
import cn.com.payu.app.config.AlipayConfigBak1;
import cn.com.payu.app.modules.converter.PaymentConfigConverter;
import cn.com.payu.app.modules.entity.PaymentConfig;
import cn.com.payu.app.modules.mapper.PaymentConfigMapper;
import cn.com.payu.app.modules.model.AlipayConfigModel;
import cn.com.payu.app.modules.model.UnifiedPayModel;
import cn.com.payu.app.modules.model.UnifiedPayResult;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.glsx.plat.common.utils.StringUtils;
import com.glsx.plat.web.utils.IpUtils;
import com.glsx.plat.wechat.modules.model.WxPayOrder;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UnifiedPayService {

    @Value("${wx.pay.notifyUrl}")
    private String wxPayNotifyUrl;

    @Resource
    private HttpServletRequest request;

    @Resource
    private WxPayService wxPayService;

    @Autowired
    private AlipayConfig alipayConfig;

    @Autowired
    private AlipayConfigBak1 alipayConfigBak1;

    @Resource
    private PaymentConfigMapper paymentConfigMapper;

    /**
     * 预支付请求
     *
     * @param model
     * @return
     */
    public Object unifiedPrePay(UnifiedPayModel model) {
        log.info("准备支付参数：{}", model.toString());
        if (OrderChannel.WECHAT_PAY.getType().equals(model.getType())) {
            WxPayOrder order = WxPayOrder.builder()
                    .tradeNo(model.getTradeNo())
                    .body(model.getSubject())
                    .totalFee(model.getTotalAmount().multiply(new BigDecimal(100)).intValue())
                    .notifyUrl(wxPayNotifyUrl)
                    .build();
            try {
                return wxPay(order);
            } catch (WxPayException e) {
                throw new AppServerException("支付发生错误，请稍后再试");
            }
        } else if (OrderChannel.ALI_PAY.getType().equals(model.getType())) {
            AlipayTradeAppPayModel order = new AlipayTradeAppPayModel();
            //业务参数传入,可以传很多，参考API
            //model.setPassbackParams(URLEncoder.encode(request.getBody().toString())); //公用参数（附加数据）
            //对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
            order.setBody(model.getSubject());
            //商品名称
            order.setSubject(model.getSubject());
            //商户订单号(根据业务需求自己生成)
            order.setOutTradeNo(model.getTradeNo());
            //交易超时时间 这里的30m就是30分钟
            order.setTimeoutExpress("30m");
            //支付金额 后面保留2位小数点..不能超过2位
            order.setTotalAmount(model.getTotalAmount().toPlainString());
            //销售产品码（固定值） //这个不做多解释..看文档api接口参数解释
            order.setProductCode("QUICK_MSECURITY_PAY");
            try {
                return aliPay(order);
            } catch (AlipayApiException e) {
                throw new AppServerException("支付发生错误，请稍后再试");
            }
        } else if (OrderChannel.NATIVE_PAY.getType().equals(model.getType())) {
            return nativePay(model);
        }
        return null;
    }

    /**
     * 自平台内交易
     */
    public UnifiedPayResult nativePay(UnifiedPayModel model) {

        log.info("本地支付参数：{}", model.toString());

        UnifiedPayResult result = new UnifiedPayResult();
        // TODO: 2020/12/17 转换成 UnifiedPayResult

        return result;
    }

    /**
     * 微信支付
     *
     * @param order
     * @return
     * @throws WxPayException
     */
    public Object wxPay(WxPayOrder order) throws WxPayException {
        if (StringUtils.isNullOrEmpty(order.getBody())
                || StringUtils.isNullOrEmpty(order.getTradeNo())
                || Objects.isNull(order.getTotalFee())
                || StringUtils.isNullOrEmpty(order.getNotifyUrl())) {
            throw WxPayException.newBuilder().build();
        }

        log.info("微信支付参数：{}", order.toString());
        final WxPayUnifiedOrderRequest unifiedOrderRequest = WxPayUnifiedOrderRequest.newBuilder()
                .body(order.getBody())
                .outTradeNo(order.getTradeNo())
                .totalFee(order.getTotalFee())
                .spbillCreateIp(IpUtils.getIpAddr(request))
                .tradeType(WxPayConstants.TradeType.APP)
                .notifyUrl(order.getNotifyUrl())
                .build();
        unifiedOrderRequest.setSignType(WxPayConstants.SignType.MD5);
        return this.wxPayService.<WxPayMpOrderResult>createOrder(unifiedOrderRequest);
    }

    public void updateWxPayOrder(WxPayOrderNotifyResult result) {
        if (result != null) {
            //我方发起支付时订单号的信息，取出来
            String outTradeNo = result.getOutTradeNo();
            //微信交易id
            String payId = result.getTransactionId();
            //分转化成元
            String totalFee = BaseWxPayResult.fenToYuan(result.getTotalFee());

            //todo 更新订单信息
            //2020/12/28 充值成功，分润处理，更新钱包余额

            log.info("订单{}支付成功", outTradeNo);
        }
    }

    private List<AlipayConfigModel> getAllAlipayConfig() {
        List<AlipayConfigModel> list = Lists.newArrayList();
        list.add(PaymentConfigConverter.INSTANCE.bean2model(alipayConfig));
        list.add(PaymentConfigConverter.INSTANCE.bean2model(alipayConfigBak1));
        return list;
    }

    private AlipayConfigModel getAlipayConfig() {
        PaymentConfig config = paymentConfigMapper.selectUsedConfig();
        if (config != null) {
            if (config.getAppId().equals(alipayConfig.getAppId())) {
                return PaymentConfigConverter.INSTANCE.bean2model(alipayConfig);
            } else if (config.getAppId().equals(alipayConfigBak1.getAppId())) {
                return PaymentConfigConverter.INSTANCE.bean2model(alipayConfigBak1);
            }
        }
        //默认支付宝账号
        return PaymentConfigConverter.INSTANCE.bean2model(alipayConfig);
    }

    /**
     * 支付宝
     *
     * @param order
     */
    public Object aliPay(AlipayTradeAppPayModel order) throws AlipayApiException {
        log.info("支付宝支付参数：{}", order.toString());

        AlipayConfigModel configModel = getAlipayConfig();

        // 支付宝网关
        String serverUrl = configModel.getGatewayUrl();
        // APPID
        String appId = configModel.getAppId();
        // 商户私钥, 即PKCS8格式RSA2私钥
        String privateKey = configModel.getPrivateKey();
        // 格式化为 json 格式
        String format = "json";
        // 字符编码格式
        String charset = configModel.getCharset();
        // 支付宝公钥, 即对应APPID下的支付宝公钥
        String alipayPublicKey = configModel.getPublicKey();
        // 签名方式
        String signType = configModel.getSignType();
        // 服务器异步通知页面路径
        String notifyUrl = configModel.getNotifyUrl();

        // 1、获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(serverUrl, appId, privateKey, format, charset, alipayPublicKey, signType);

        // 2、设置请求参数
        AlipayTradeAppPayRequest alipayRequest = new AlipayTradeAppPayRequest();
        // 页面跳转同步通知页面路径
//        alipayRequest.setReturnUrl(returnUrl);
        // 服务器异步通知页面路径
        alipayRequest.setNotifyUrl(notifyUrl);
        // 封装参数(以json格式封装)
//        alipayRequest.setBizContent(JSON.toJSONString(order));
        alipayRequest.setBizModel(order);

        // 3、请求支付宝进行付款，并获取支付结果
        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(alipayRequest);
        if (response.isSuccess()) {
            //就是orderString 可以直接给客户端请求，无需再做处理。
            log.info("支付宝预支付BODY: {}", response.getBody());
            return response.getBody();
        }
        return null;
    }

    /**
     * 多个支付宝，验证一个通过就行
     *
     * @param params
     * @return
     * @throws AlipayApiException
     */
    public boolean verifyAlipaySign(Map<String, String> params) throws AlipayApiException {
        String sign = params.get("sign");
        boolean signVerified1 = AlipaySignature.rsaCheckV1(params, alipayConfig.getPublicKey(), alipayConfig.getCharset(), alipayConfig.getSignType());
        if (!params.containsKey("sign")) {
            params.put("sign", sign);
        }
        boolean signVerified2 = AlipaySignature.rsaCheckV1(params, alipayConfigBak1.getPublicKey(), alipayConfigBak1.getCharset(), alipayConfigBak1.getSignType());
        return signVerified1 || signVerified2;
    }

    public String updateAlipayOrder(Map<String, String> params) {
        log.info("支付宝回调参数 {}", params.toString());
        String appId = params.get("app_id");
        String orderNo = params.get("out_trade_no");
//        PayOrderDTO order = orderService.getOrderById(orderId);
//     如果订单不存在，则支付操作无意义
//     不让支付宝再继续调用异步通知（返回为 SUCCESS 后，支付宝将不再调用）。
//        if (order == null) {
//            return "success";
//        }
        // 判断订单状态是否已经被修改
//        int orderStatus = orderService.getOrderStatus(orderId);
//        if (orderStatus == 1){
//            return "success";
//        }
        String tradeStatus = params.get("trade_status");
        // 支付成功
        if ("TRADE_SUCCESS".equals(tradeStatus)) {
            // 更新订单信息
            // 2020/12/28 充值成功，分润处理，更新钱包余额
            // todo 更新订单状态
            log.info("订单{}支付成功，支付AppId {}", orderNo, appId);

            return "success";
        }
        return "failure";
    }

}
