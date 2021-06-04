package priv.yjs.umbrellasharing.model.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Avatar {

    @NotNull
    private String avatar;
}
