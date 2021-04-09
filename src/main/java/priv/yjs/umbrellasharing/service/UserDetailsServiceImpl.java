package priv.yjs.umbrellasharing.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import priv.yjs.umbrellasharing.common.ResultType;
import priv.yjs.umbrellasharing.exception.GlobalException;
import priv.yjs.umbrellasharing.model.entity.LoginUser;
import priv.yjs.umbrellasharing.model.entity.User;

import javax.annotation.Resource;

/**
 * @author Anyu
 * @since 2021/3/24
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserService userService;

    /**
     * 用户验证
     *
     * @param username 账户名
     * @return 登录用户信息
     * @throws UsernameNotFoundException 用户存不存在异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        return userService.getUserByUsername(username)
                .map(this::createLoginUser)
                .orElseThrow(() -> GlobalException.causeBy(ResultType.USER_NOT_EXISTED));
    }

    private UserDetails createLoginUser(User user) {
        var loginUser = new LoginUser();
        loginUser.setUser(user);
        return loginUser;
    }
}
