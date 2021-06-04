package priv.yjs.umbrellasharing.model.param;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import lombok.Getter;
import lombok.Setter;
import priv.yjs.umbrellasharing.common.BasePageCondition;
import priv.yjs.umbrellasharing.model.entity.Umbrella;

@Setter
@Getter
public class UmbrellaPageCondition extends BasePageCondition<Umbrella> {
    /**
     * 1 free 0 busy
     */
    private Boolean idle;
    /**
     * 放置点id
     */
    private Long pmId;

    public  LambdaQueryChainWrapper<Umbrella> initWrapper(LambdaQueryChainWrapper<Umbrella> lambdaQuery){
        return lambdaQuery
                .eq(pmId != null && pmId > 0, Umbrella::getPmId, pmId)
                .eq(idle != null , Umbrella::getIdle, idle);
    }


}
