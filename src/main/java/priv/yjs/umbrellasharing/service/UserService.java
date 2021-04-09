package priv.yjs.umbrellasharing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import priv.yjs.umbrellasharing.common.ResultType;
import priv.yjs.umbrellasharing.exception.GlobalException;
import priv.yjs.umbrellasharing.mapper.UserMapper;
import priv.yjs.umbrellasharing.model.entity.User;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

/**
 * 用户服务层
 *
 * @author Anyu
 * @since 2021/3/24
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> implements IService<User> {

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Optional<User> getUserByUsername(String username) {
        return lambdaQuery().eq(StringUtils.isNotBlank(username), User::getUsername, username).oneOpt();
    }

    /**
     * 用户注册
     *
     * @param user 用户信息
     */
    public boolean register(User user) {
        if (Objects.isNull(user)) {
            return false;
        }
        //验证account
        if (getUserByUsername(user.getUsername()).isPresent()) {
            throw GlobalException.causeBy(ResultType.USER_EXISTED);
        }
        //密码hash
        hashPassword(user, user.getPassword());
        return save(user);
    }

    /**
     * hash 密码
     *
     * @param user     用户
     * @param password 输入密码
     */
    private void hashPassword(User user, String password) {
        if (Objects.isNull(user) || StringUtils.isBlank(password)) {
            throw GlobalException.causeBy(ResultType.USER_NOT_EXISTED);
        }
        final var hashPassword = bCryptPasswordEncoder.encode(password.trim());
        user.setPassword(hashPassword);
    }
}
