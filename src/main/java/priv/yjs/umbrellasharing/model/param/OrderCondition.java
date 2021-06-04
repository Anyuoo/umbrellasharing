package priv.yjs.umbrellasharing.model.param;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderCondition {
    private String pm;
    private LocalDate beginTime;
}
