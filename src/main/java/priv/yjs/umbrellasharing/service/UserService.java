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
import priv.yjs.umbrellasharing.model.param.PasswordInput;
import priv.yjs.umbrellasharing.model.param.UserUpdateInput;

import javax.annotation.Resource;
import java.util.List;
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

    public Optional<User> getUserById(long id) {
        return lambdaQuery().eq(User::getId, id).oneOpt();
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

    public boolean updateUserInfo(long loginUserId, UserUpdateInput input) {
        User loginUser = getById(loginUserId);
        if (loginUser == null) {
            return false;
        }
        loginUser.setNickname(input.getNickname())
                .setGender(input.getGender())
                .setBirthday(input.getBirthday());
        return updateById(loginUser);
    }

    public boolean updatePassword(long loginUserId, PasswordInput input) {
        User user = getUserById(loginUserId).orElseThrow(() -> GlobalException.causeBy(ResultType.USER_NOT_EXISTED));
        if (bCryptPasswordEncoder.matches(input.getOldPass(), user.getPassword())) {
            final var newPass = bCryptPasswordEncoder.encode(input.getNewPass().trim());
            user.setPassword(newPass);
            return updateById(user);
        }
        return false;
    }

    public boolean updateAvatar(long validLUId, String avatar) {
        User user = getById(validLUId).setAvatar(avatar);
        return updateById(user);
    }

    public List<User> listAll(String username, String nickname) {
        return lambdaQuery()
                .like(StringUtils.isNotBlank(username),User::getUsername,username)
                .like(StringUtils.isNotBlank(nickname),User::getNickname,nickname).list();
    }
}
