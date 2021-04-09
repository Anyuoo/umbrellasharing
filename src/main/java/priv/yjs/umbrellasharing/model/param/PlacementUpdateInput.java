package priv.yjs.umbrellasharing.model.param;

import lombok.Getter;
import lombok.Setter;
import priv.yjs.umbrellasharing.model.entity.Placement;

/**
 * @author Anyu
 * @since 2021/4/8
 */
@Getter
@Setter
public class PlacementUpdateInput {
    private Long id;
    private String position;
    private Integer umbrellaTotal;

    public Placement toEntity() {
        return new Placement()
                .setId(id)
                .setPosition(position)
                .setUmbrellaTotal(umbrellaTotal);
    }
}
