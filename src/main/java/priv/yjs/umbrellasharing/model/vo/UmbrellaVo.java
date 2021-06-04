package priv.yjs.umbrellasharing.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UmbrellaVo {
    private Long id;
    /**
     * 放置点
     */
    private String  pmName;

    private Integer times;
    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 1 free 0 busy
     */
    private Boolean idle;
}
