package priv.yjs.umbrellasharing.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.enums.TradeType;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.IpKit;
import com.ijpay.core.kit.QrCodeKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.model.UnifiedOrderModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import priv.yjs.umbrellasharing.annotation.CommonResultHandler;
import priv.yjs.umbrellasharing.common.ResultType;
import priv.yjs.umbrellasharing.service.PayService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 微信支付接口
 *
 * @author Anyu
 * @since 2021/4/8
 */
@Api(tags = "支付API")
@RestController
@RequestMapping(path = "/payments")
@CommonResultHandler
public class PayController {
    @Resource
    private WxPayApiConfig wxPayApiConfig;
    @Resource
    private PayService payService;

    @ApiOperation("微信支付")
    @PostMapping
    public ResultType pay() {
        if (payService.pay()) {
            return ResultType.SUCCESS;
        }
        return ResultType.FAIL;
    }

    /**
     * 扫码支付
     */
    @ApiOperation("微信支付")
    @PostMapping(path = "/wx")
    public Optional<String> scanCode(HttpServletRequest request, HttpServletResponse response, @RequestParam("order_id") String orderId) {
        try {
            if (StringUtils.isBlank(orderId)) {
//                return new AjaxResult().addError("productId is null");
            }

            //获取扫码支付（模式一）url
            var qrCodeUrl = WxPayKit.bizPayUrl(wxPayApiConfig.getPartnerKey(), wxPayApiConfig.getAppId(), wxPayApiConfig.getMchId(), orderId);
            //生成二维码保存的路径
            String name = "WxPayQR.jpg";
            boolean encode = QrCodeKit.encode(qrCodeUrl, BarcodeFormat.QR_CODE, 3, ErrorCorrectionLevel.H,
                    "png", 200, 200,
                    ResourceUtils.getURL("classpath:").getPath().concat("static").concat(File.separator).concat(name));
            if (encode) {
                //在页面上显示
                return Optional.of(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
//            return new AjaxResult().addError("系统异常：" + e.getMessage());
        }
        return null;
    }

    /**
     * 扫码支付模式一回调
     */
    @ApiOperation("微信支付回调地址")
    @PostMapping(value = "/scanCodeNotify")
    public String scanCodeNotify(HttpServletRequest request, HttpServletResponse response) {
        try {
            String result = HttpKit.readData(request);
            // 获取返回的信息内容中各个参数的值
            Map<String, String> map = WxPayKit.xmlToMap(result);

            String appId = map.get("appid");
            String openId = map.get("openid");
            String mchId = map.get("mch_id");
            String isSubscribe = map.get("is_subscribe");
            String nonceStr = map.get("nonce_str");
            String orderId = map.get("order_id");
            String sign = map.get("sign");

            Map<String, String> packageParams = new HashMap<>(8);
            packageParams.put("appid", appId);
            packageParams.put("openid", openId);
            packageParams.put("mch_id", mchId);
            packageParams.put("is_subscribe", isSubscribe);
            packageParams.put("nonce_str", nonceStr);
            packageParams.put("order_id", orderId);


            String packageSign = WxPayKit.createSign(packageParams, wxPayApiConfig.getPartnerKey(), SignType.MD5);

            String ip = IpKit.getRealIp(request);
            if (StringUtils.isBlank(ip)) {
                ip = "127.0.0.1";
            }
            //
            String notifyUrl = wxPayApiConfig.getDomain().concat("/wxPay/payNotify");

            Map<String, String> params = UnifiedOrderModel
                    .builder()
                    .appid(wxPayApiConfig.getAppId())
                    .mch_id(wxPayApiConfig.getMchId())
                    .nonce_str(WxPayKit.generateStr())
                    .body("IJPay 让支付触手可及-扫码支付模式一")
                    .attach("Node.js 版:https://gitee.com/javen205/TNWX")
                    .out_trade_no(WxPayKit.generateStr())
                    .total_fee("1")
                    .spbill_create_ip(ip)
                    .notify_url(notifyUrl)
                    .trade_type(TradeType.NATIVE.getTradeType())
                    .openid(openId)
                    .build()
                    .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);
            String xmlResult = WxPayApi.pushOrder(false, params);
//            log.info("统一下单:" + xmlResult);
            /**
             * 发送信息给微信服务器
             */
            Map<String, String> payResult = WxPayKit.xmlToMap(xmlResult);
            String returnCode = payResult.get("return_code");
            String resultCode = payResult.get("result_code");
            if (WxPayKit.codeIsOk(returnCode) && WxPayKit.codeIsOk(resultCode)) {
                // 以下字段在 return_code 和 result_code 都为 SUCCESS 的时候有返回
                String prepayId = payResult.get("prepay_id");

                Map<String, String> prepayParams = new HashMap<>();
                prepayParams.put("return_code", "SUCCESS");
                prepayParams.put("appid", appId);
                prepayParams.put("mch_id", mchId);
                prepayParams.put("nonce_str", System.currentTimeMillis() + "");
                prepayParams.put("prepay_id", prepayId);
                String prepaySign;
                if (sign.equals(packageSign)) {
                    prepayParams.put("result_code", "SUCCESS");
                } else {
                    prepayParams.put("result_code", "FAIL");
                    //result_code为FAIL时，添加该键值对，value值是微信告诉客户的信息
                    prepayParams.put("err_code_des", "订单失效");
                }
                prepaySign = WxPayKit.createSign(prepayParams, wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);
                prepayParams.put("sign", prepaySign);
                String xml = WxPayKit.toXml(prepayParams);
//                log.error(xml);
                return xml;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
