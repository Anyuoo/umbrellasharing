package priv.yjs.umbrellasharing.config.payment;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
/**
 * @author Anyu
 * @since 2021/4/8
 */

/**
 * 微信支付属性
 *
 * @author Anyu
 * @since 2021/4/8
 */

@Getter
@Setter
@ConfigurationProperties(prefix = "pay.wx")
public class WxPayProperties {
    private String appId;
    private String appSecret;
    private String mchId;
    private String partnerKey;
    private String certPath;
    private String domain;
}
