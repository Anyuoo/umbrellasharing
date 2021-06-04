package priv.yjs.umbrellasharing.common;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import priv.yjs.umbrellasharing.exception.GlobalException;
import priv.yjs.umbrellasharing.model.entity.LoginUser;
import priv.yjs.umbrellasharing.security.TokenService;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 当前请求持有者
 *
 * @author Anyu
 * @since 2021/4/9
 */
@Component
public class HostHolder {
    @Resource
    private TokenService tokenService;

    public Optional<LoginUser> getLoginUser() {
        final var requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return tokenService.getLoginUser(requestAttributes != null ? requestAttributes.getRequest() : null);
    }


    public long getValidLUId() {
        return getLoginUser().map(lu -> lu.getUser().getId()).orElseThrow(() -> GlobalException.causeBy(ResultType.USER_NOT_LOGIN));
    }

    public long getLUId() {
        return getLoginUser().map(lu -> lu.getUser().getId()).orElse(0L);
    }
}
