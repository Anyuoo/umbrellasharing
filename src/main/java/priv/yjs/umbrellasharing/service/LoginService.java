package priv.yjs.umbrellasharing.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import priv.yjs.umbrellasharing.model.entity.LoginUser;
import priv.yjs.umbrellasharing.security.TokenService;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 登录业务
 *
 * @author Anyu
 * @since 2021/3/24
 */
@Service
@Log4j2
public class LoginService {
    @Resource
    private TokenService tokenService;
    @Resource
    private AuthenticationManager authenticationManager;

    /**
     * 登录验证
     *
     * @param username 账号
     * @param password 密码
     * @return token
     */
    public Optional<String> login(String username, String password) {
        //用户验证,authenticate该方法会去调用UserDetailsServiceImpl.loadUserByUsername
        var authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        var loginUser = (LoginUser) authenticate.getPrincipal();
        return tokenService.createJwtToken(loginUser);
    }
}
