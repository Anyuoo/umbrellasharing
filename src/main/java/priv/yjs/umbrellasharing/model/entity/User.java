package priv.yjs.umbrellasharing.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import priv.yjs.umbrellasharing.common.BaseEntity;

import java.time.LocalDate;


/**
 * 用户
 *
 * @author Anyu
 * @since 2021/4/8
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("user")
public class User extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 账户名
     */
    @TableField
    private String username;

    private String avatar;

    private Boolean gender;

    /**
     * 昵称
     */
    private String nickname;
    /**
     * 密码
     */
    @JsonIgnore
    private String password;


    private LocalDate birthday;

}
