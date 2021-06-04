package priv.yjs.umbrellasharing.model.param;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class PasswordInput {
    @Length(min = 6, max = 20, message = "密码6~20个字符")
    private String oldPass;
    @Length(min = 6, max = 20, message = "密码6~20个字符")
    private String newPass;
}
