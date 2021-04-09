package priv.yjs.umbrellasharing.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import priv.yjs.umbrellasharing.common.BaseEntity;

/**
 * 雨伞放置节点
 *
 * @author Anyu
 * @since 2021/4/8
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Placement extends BaseEntity {
    private Long id;
    private String position;
    private Integer umbrellaTotal;
    private Integer umbrellaStock;
}
