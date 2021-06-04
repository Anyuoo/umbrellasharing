package priv.yjs.umbrellasharing.common;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.Setter;
import priv.yjs.umbrellasharing.model.entity.Umbrella;

/**
 * 分页基础条件
 *
 * @author Anyu
 * @since 2021/4/7
 */
@Getter
@Setter
public abstract class BasePageCondition<T> {

    protected long size = 10L;

    protected long current = 1L;

    /**
     * 初始化分页对象
     *
     * @return page
     */
    public Page<T> initPage() {
        return new Page<>(current, size);
    }

}
