package priv.yjs.umbrellasharing.common;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Anyu
 * @since 2021/4/8
 */
@Getter
@Setter
@Accessors(chain = true)
public abstract class BaseEntity implements Serializable {
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    protected Integer status;

    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime modifiedTime;
}
