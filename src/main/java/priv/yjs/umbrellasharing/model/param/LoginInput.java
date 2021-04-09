package priv.yjs.umbrellasharing.model.param;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author Anyu
 * @since 2021/4/9
 */
@Getter
@Setter
public class LoginInput {
    @Length(min = 4, max = 10, message = "账号4~10个字符")
    @NotBlank(message = "账号不能为空")
    private String username;

    @Length(min = 4, max = 10, message = "账号4~10个字符")
    @NotBlank(message = "密码不能为空")
    private String password;
}
