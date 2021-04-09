package priv.yjs.umbrellasharing.model.param;

import lombok.Getter;
import lombok.Setter;
import priv.yjs.umbrellasharing.model.entity.Borrow;

@Getter
@Setter
public class BorrowInput {
    /**
     * 雨伞id
     */
    private Long umbrellaId;

    /**
     * 是否归还
     */
    public Borrow toEntity() {
        return new Borrow()
                .setUmbrellaId(umbrellaId);
    }

}
