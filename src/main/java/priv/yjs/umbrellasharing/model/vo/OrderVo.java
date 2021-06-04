package priv.yjs.umbrellasharing.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderVo {

    private Long id;
    /**
     * 总花费
     */
    private BigDecimal totalPrice;
    /**
     * 支付方
     */
    private Long payerId;

    private String pmName;

    private Long umId;
    /**
     * 收款方
     */
    private String payee;
    /**
     * 是否支付（false未支付，true已支付）
     */
    private Boolean hasPaid;

    private LocalDateTime borrowTime;

    private LocalDateTime returnTime;

    private String borrowPm;

    private String returnPm;
}
