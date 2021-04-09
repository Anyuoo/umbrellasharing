package priv.yjs.umbrellasharing.model.param;

import lombok.Getter;
import lombok.Setter;
import priv.yjs.umbrellasharing.model.entity.Umbrella;

import java.math.BigDecimal;

@Getter
@Setter
public class UmbrellaUpdateInput {
    private Long id;
    /**
     * 放置点id
     */
    private Long pmId;
    /**
     * 单价
     */
    private BigDecimal price;

    public Umbrella toEntity() {
        return new Umbrella()
                .setId(id)
                .setPmId(pmId)
                .setPrice(price);
    }
}
