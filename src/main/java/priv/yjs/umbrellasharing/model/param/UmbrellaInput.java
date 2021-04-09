package priv.yjs.umbrellasharing.model.param;

import lombok.Getter;
import lombok.Setter;
import priv.yjs.umbrellasharing.model.entity.Umbrella;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 雨伞表单信息
 *
 * @author Anyu
 * @since 2021/4/7
 */
@Getter
@Setter
public class UmbrellaInput {

    /**
     * 单价
     */
    @NotNull(message = "价格不能为空")
    private BigDecimal price;

    public Umbrella toEntity() {
        return new Umbrella()
                .setPrice(price);
    }
}
