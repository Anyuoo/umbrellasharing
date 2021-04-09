package priv.yjs.umbrellasharing.config.payment;

import com.ijpay.wxpay.WxPayApiConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 微信支付配置
 *
 * @author Anyu
 * @since 2021/4/8
 */
@Configuration
@EnableConfigurationProperties(WxPayProperties.class)
public class WxPayConfig {
    @Resource
    private WxPayProperties wxPayProperties;

    @Bean
    public WxPayApiConfig wxPayApiConfig() {
        return WxPayApiConfig.builder()
                .appId(wxPayProperties.getAppId())
                .mchId(wxPayProperties.getMchId())
                .partnerKey(wxPayProperties.getPartnerKey())
                .certPath(wxPayProperties.getCertPath())
                .domain(wxPayProperties.getDomain())
                .build();
    }
}
