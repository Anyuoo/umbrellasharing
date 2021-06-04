package priv.yjs.umbrellasharing.model.param;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class UserUpdateInput {

    @Length(min = 2, max = 10, message = "昵称2~10个字符")
    private String nickname;
    @NotNull(message = "")
    private Boolean gender;

    @NotNull(message = "")
    private LocalDate birthday;
}
