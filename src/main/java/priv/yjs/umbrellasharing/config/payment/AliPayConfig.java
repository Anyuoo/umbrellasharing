package priv.yjs.umbrellasharing.config.payment;

import com.ijpay.alipay.AliPayApiConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@EnableConfigurationProperties(AliPayProperties.class)
public class AliPayConfig {
    @Resource
    private AliPayProperties aliPayProperties;

    @Bean
    public AliPayApiConfig aliPayApiConfig() {
       return AliPayApiConfig.builder()
                .setAppId(aliPayProperties.getAppId())
                .setAliPayPublicKey(aliPayProperties.getPublicKey())
                .setAppCertPath(aliPayProperties.getAppCertPath())
                .setAliPayCertPath(aliPayProperties.getAliPayCertPath())
                .setAliPayRootCertPath(aliPayProperties.getAliPayRootCertPath())
                .setCharset("UTF-8")
                .setPrivateKey(aliPayProperties.getPrivateKey())
                .setServiceUrl(aliPayProperties.getServerUrl())
                .setSignType("RSA2")
                // 普通公钥方式
                .build();
    }
}
