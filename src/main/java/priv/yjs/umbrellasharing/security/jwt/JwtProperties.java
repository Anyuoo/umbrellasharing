package priv.yjs.umbrellasharing.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * jwt 参数配置
 *
 * @author Anyu
 * @since 2021/3/24
 */
@ConfigurationProperties(prefix = "jwt", ignoreInvalidFields = true)
@Getter
@Setter
public class JwtProperties {
    //秘钥
    private String secretKey = "7eb374edfac7cc268f7dab26c8595579576959ba53ca50d2c11a324e";
    //jwt接收对象
    private String aud;
    //jwt的签发主体
    private String iss;
    //过期时间
    @DurationUnit(ChronoUnit.HOURS)
    private Duration expiredHours = Duration.ofHours(24);

    private String authHeaderKey = "token";

    private String tokenPrefix = "";
}
