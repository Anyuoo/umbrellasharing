package priv.yjs.umbrellasharing.service;

import org.springframework.stereotype.Service;
import priv.yjs.umbrellasharing.common.HostHolder;

import javax.annotation.Resource;

/**
 * 支付服务层
 *
 * @author Anyu
 * @since 2021/4/9
 */
@Service
public class PayService {
    @Resource
    private OrderService orderService;
    @Resource
    private HostHolder hostHolder;

    /**
     * 支付
     */
    public boolean pay() {
        return orderService.updateOrderPayStatus(hostHolder.getValidLUId());
    }

}
