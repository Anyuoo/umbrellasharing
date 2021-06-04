package priv.yjs.umbrellasharing.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import priv.yjs.umbrellasharing.common.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单表
 *
 * @author Anyu
 * @since 2021/4/8
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("`order`")
public class Order extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 总花费
     */
    private BigDecimal totalPrice;
    /**
     * 支付方
     */
    private Long payerId;

    private Long brwId;
    /**
     * 收款方
     */
    private String payee;
    /**
     * 是否支付（false未支付，true已支付）
     */
    private Boolean hasPaid;


}
