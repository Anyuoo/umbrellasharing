package priv.yjs.umbrellasharing.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 分页基础条件
 *
 * @author Anyu
 * @since 2021/4/7
 */
public abstract class BasePageCondition<T> {

    protected long size = 20L;
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
