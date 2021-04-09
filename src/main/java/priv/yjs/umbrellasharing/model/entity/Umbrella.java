package priv.yjs.umbrellasharing.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import priv.yjs.umbrellasharing.common.BaseEntity;

import java.math.BigDecimal;

/**
 * 雨伞信息
 *
 * @author Anyu
 * @since 2021/4/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("umbrella")
@Accessors(chain = true)
public class Umbrella extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 放置点id
     */
    private Long pmId;
    /**
     * 单价
     */
    private BigDecimal price;
}
