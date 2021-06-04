package priv.yjs.umbrellasharing.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.*;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.ijpay.alipay.AliPayApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import priv.yjs.umbrellasharing.common.CommonUtils;
import priv.yjs.umbrellasharing.common.HostHolder;
import priv.yjs.umbrellasharing.common.ResultType;
import priv.yjs.umbrellasharing.config.payment.AliPayProperties;
import priv.yjs.umbrellasharing.exception.GlobalException;
import priv.yjs.umbrellasharing.model.entity.Order;
import priv.yjs.umbrellasharing.service.OrderService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@RestController
@Api(tags = "支付模块")
@RequestMapping("pay")
public class AliPayController {
    private static final Logger log = LoggerFactory.getLogger(AliPayController.class);

    @Resource
    private AliPayProperties aliPayProperties;
    @Resource
    private OrderService orderService;
    @Resource
    private HostHolder hostHolder;

    // 普通公钥模式
     private final static String NOTIFY_URL = ":9001/api/pay/notify_url";
//    private final static String RETURN_URL = "/pay/return_url";
    private final static String RETURN_URL = ":8080/#/index";


    /**
     * PC支付
     */
    @PostMapping(path = "/pc-pay")
    @ApiOperation("PC支付")
    public void pcPay(HttpServletResponse response) {
        Order order = orderService.getUnpaidOrderByUserId(hostHolder.getValidLUId()).orElseThrow(() -> GlobalException.causeBy(ResultType.FAIL));
        try {
            String totalAmount = order.getTotalPrice().toString();
            String outTradeNo = CommonUtils.getOutTradeNo();
            
            String returnUrl = aliPayProperties.getDomain() + RETURN_URL;
            String notifyUrl = aliPayProperties.getDomain() + NOTIFY_URL;
            AlipayTradePagePayModel model = new AlipayTradePagePayModel();

            model.setOutTradeNo(outTradeNo);
            model.setProductCode("FAST_INSTANT_TRADE_PAY");
            model.setTotalAmount(totalAmount);
            model.setSubject("共享雨伞支付");
            model.setBody("共享雨伞支付");
            model.setPassbackParams("passback_params");
            /**
             * 花呗分期相关的设置,测试环境不支持花呗分期的测试
             * hb_fq_num代表花呗分期数，仅支持传入3、6、12，其他期数暂不支持，传入会报错；
             * hb_fq_seller_percent代表卖家承担收费比例，商家承担手续费传入100，用户承担手续费传入0，仅支持传入100、0两种，其他比例暂不支持，传入会报错。
             */
//            ExtendParams extendParams = new ExtendParams();
//            extendParams.setHbFqNum("3");
//            extendParams.setHbFqSellerPercent("0");
//            model.setExtendParams(extendParams);

            AliPayApi.tradePage(response, model, notifyUrl, returnUrl);
            orderService.updateOrderPayStatus(hostHolder.getValidLUId());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    /**
     * 扫码支付
     */
    @PostMapping(path = "/pre-pay")
    @ApiOperation("Javen 支付宝扫码支付测试\"")
    public String tradePreCreatePay() {
        String subject = "Javen 支付宝扫码支付测试";
        String totalAmount = "86";
        String storeId = "123";
//        String notifyUrl = aliPayBean.getDomain() + NOTIFY_URL;
        String notifyUrl = aliPayProperties.getDomain() + "/pay/cert_notify_url";

        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setSubject(subject);
        model.setTotalAmount(totalAmount);
        model.setStoreId(storeId);
        model.setTimeoutExpress("5m");
        model.setOutTradeNo(CommonUtils.getOutTradeNo());
        try {
            String resultStr = AliPayApi.tradePrecreatePayToResponse(model, notifyUrl).getBody();
            JSONObject jsonObject = JSONObject.parseObject(resultStr);
            return jsonObject.getJSONObject("alipay_trade_precreate_response").getString("qr_code");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


 /**
     * 创建订单
     * {"alipay_trade_create_response":{"code":"10000","msg":"Success","out_trade_no":"081014283315033","trade_no":"2017081021001004200200274066"},"sign":"ZagfFZntf0loojZzdrBNnHhenhyRrsXwHLBNt1Z/dBbx7cF1o7SZQrzNjRHHmVypHKuCmYifikZIqbNNrFJauSuhT4MQkBJE+YGPDtHqDf4Ajdsv3JEyAM3TR/Xm5gUOpzCY7w+RZzkHevsTd4cjKeGM54GBh0hQH/gSyhs4pEN3lRWopqcKkrkOGZPcmunkbrUAF7+AhKGUpK+AqDw4xmKFuVChDKaRdnhM6/yVsezJFXzlQeVgFjbfiWqULxBXq1gqicntyUxvRygKA+5zDTqE5Jj3XRDjVFIDBeOBAnM+u03fUP489wV5V5apyI449RWeybLg08Wo+jUmeOuXOA=="}
     */
    @RequestMapping(value = "/tradeCreate")
    @ResponseBody
    public String tradeCreate(@RequestParam("outTradeNo") String outTradeNo) {

        String notifyUrl = aliPayProperties.getDomain() + NOTIFY_URL;

        AlipayTradeCreateModel model = new AlipayTradeCreateModel();
        model.setOutTradeNo(outTradeNo);
        model.setTotalAmount("88.88");
        model.setBody("Body");
        model.setSubject("Javen 测试统一收单交易创建接口");
        //买家支付宝账号，和buyer_id不能同时为空
        model.setBuyerLogonId("abpkvd0206@sandbox.com");
        try {
            AlipayTradeCreateResponse response = AliPayApi.tradeCreateToResponse(model, notifyUrl);
            return response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 撤销订单
     */
    @RequestMapping(value = "/tradeCancel")
    @ResponseBody
    public String tradeCancel(@RequestParam(required = false, name = "outTradeNo") String outTradeNo, @RequestParam(required = false, name = "tradeNo") String tradeNo) {
        try {
            AlipayTradeCancelModel model = new AlipayTradeCancelModel();
            if (StringUtils.isNotEmpty(outTradeNo)) {
                model.setOutTradeNo(outTradeNo);
            }
            if (StringUtils.isNotEmpty(tradeNo)) {
                model.setTradeNo(tradeNo);
            }

            return AliPayApi.tradeCancelToResponse(model).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭订单
     */
    @RequestMapping(value = "/tradeClose")
    @ResponseBody
    public String tradeClose(@RequestParam("outTradeNo") String outTradeNo, @RequestParam("tradeNo") String tradeNo) {
        try {
            AlipayTradeCloseModel model = new AlipayTradeCloseModel();
            if (StringUtils.isNotEmpty(outTradeNo)) {
                model.setOutTradeNo(outTradeNo);
            }
            if (StringUtils.isNotEmpty(tradeNo)) {
                model.setTradeNo(tradeNo);
            }

            return AliPayApi.tradeCloseToResponse(model).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 结算
     */
    @RequestMapping(value = "/tradeOrderSettle")
    @ResponseBody
    public String tradeOrderSettle(@RequestParam("tradeNo") String tradeNo) {
        try {
            AlipayTradeOrderSettleModel model = new AlipayTradeOrderSettleModel();
            model.setOutRequestNo(CommonUtils.getOutTradeNo());
            model.setTradeNo(tradeNo);

            return AliPayApi.tradeOrderSettleToResponse(model).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用授权URL并授权
     */
    @RequestMapping(value = "/toOauth")
    @ResponseBody
    public void toOauth(HttpServletResponse response) {
        try {
            String redirectUri = aliPayProperties.getDomain() + "/aliPay/redirect_uri";
            String oauth2Url = AliPayApi.getOauth2Url(aliPayProperties.getAppId(), redirectUri);
            response.sendRedirect(oauth2Url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 应用授权回调
     */
    @RequestMapping(value = "/redirect_uri")
    @ResponseBody
    public String redirectUri(@RequestParam("app_id") String appId, @RequestParam("app_auth_code") String appAuthCode) {
        try {
            System.out.println("app_id:" + appId);
            System.out.println("app_auth_code:" + appAuthCode);
            //使用app_auth_code换取app_auth_token
            AlipayOpenAuthTokenAppModel model = new AlipayOpenAuthTokenAppModel();
            model.setGrantType("authorization_code");
            model.setCode(appAuthCode);
            return AliPayApi.openAuthTokenAppToResponse(model).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    @RequestMapping(value = "/cert_return_url")
    @ResponseBody
    public String certReturnUrl(HttpServletRequest request) {
        try {
            // 获取支付宝GET过来反馈信息
            Map<String, String> map = AliPayApi.toMap(request);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }

            boolean verifyResult = AlipaySignature.rsaCertCheckV1(map, aliPayProperties.getAliPayCertPath(), "UTF-8",
                    "RSA2");

            if (verifyResult) {
                // TODO 请在这里加上商户的业务逻辑程序代码
                System.out.println("certReturnUrl 验证成功");

                return "success";
            } else {
                System.out.println("certReturnUrl 验证失败");
                // TODO
                return "failure";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "failure";
        }
    }


    @PostMapping(path = "/notify_url")
    public String notifyUrl(HttpServletRequest request) {
        try {
            // 获取支付宝POST过来反馈信息
            Map<String, String> params = AliPayApi.toMap(request);

            for (Map.Entry<String, String> entry : params.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }

            boolean verifyResult = AlipaySignature.rsaCheckV1(params, aliPayProperties.getPublicKey(), "UTF-8", "RSA2");

            if (verifyResult) {
                // TODO 请在这里加上商户的业务逻辑程序代码 异步通知可能出现订单重复通知 需要做去重处理
                System.out.println("notify_url 验证成功succcess");
                return "success";
            } else {
                System.out.println("notify_url 验证失败");
                // TODO
                return "failure";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "failure";
        }
    }

    @RequestMapping(value = "/cert_notify_url")
    @ResponseBody
    public String certNotifyUrl(HttpServletRequest request) {
        try {
            // 获取支付宝POST过来反馈信息
            Map<String, String> params = AliPayApi.toMap(request);

            for (Map.Entry<String, String> entry : params.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }

            boolean verifyResult = AlipaySignature.rsaCertCheckV1(params, aliPayProperties.getAliPayCertPath(), "UTF-8", "RSA2");

            if (verifyResult) {
                // TODO 请在这里加上商户的业务逻辑程序代码 异步通知可能出现订单重复通知 需要做去重处理
                System.out.println("certNotifyUrl 验证成功succcess");
                return "success";
            } else {
                System.out.println("certNotifyUrl 验证失败");
                // TODO
                return "failure";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "failure";
        }
    }

}