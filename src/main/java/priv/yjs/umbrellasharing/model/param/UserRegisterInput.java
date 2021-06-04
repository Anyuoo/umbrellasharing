package priv.yjs.umbrellasharing.model.param;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import priv.yjs.umbrellasharing.model.entity.User;

import javax.validation.constraints.NotBlank;

/**
 * 用户注册信息
 *
 * @author Anyu
 * @since 2021/3/24
 */
@Getter
@Setter
public class UserRegisterInput {
    @Length(min = 4, max = 10, message = "账号4~10个字符")
    @NotBlank(message = "账号不能为空")
    private String username;

    @Length(min = 4, max = 10, message = "昵称4~10个字符")
    private String nickname;

    @Length(min = 6, max = 20, message = "密码6~20个字符")
    private String password;

    public User toEntity() {
        return new User()
                .setGender(false)
                .setUsername(username)
                .setNickname(nickname)
                .setPassword(password);
    }
}
