package priv.yjs.umbrellasharing.model.param;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class PlacementUmbrellasInput {
    @NotNull
    private Long pmId;

    @NotEmpty
    List<Long> umbrellaIds;
}
