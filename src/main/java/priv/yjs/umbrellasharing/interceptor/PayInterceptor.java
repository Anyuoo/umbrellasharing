package priv.yjs.umbrellasharing.interceptor;

import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import priv.yjs.umbrellasharing.config.payment.AliPayProperties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class PayInterceptor implements HandlerInterceptor {
    @Resource
    private AliPayApiConfig aliPayApiConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AliPayApiConfigKit.setThreadLocalAliPayApiConfig(aliPayApiConfig);
        return true;
    }
}
