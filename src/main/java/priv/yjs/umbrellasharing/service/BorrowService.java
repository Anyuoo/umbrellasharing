package priv.yjs.umbrellasharing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import priv.yjs.umbrellasharing.common.HostHolder;
import priv.yjs.umbrellasharing.common.ResultType;
import priv.yjs.umbrellasharing.exception.GlobalException;
import priv.yjs.umbrellasharing.mapper.BorrowMapper;
import priv.yjs.umbrellasharing.model.entity.Borrow;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 借还服务层
 *
 * @author Anyu
 * @since 2021/4/8
 */
@Service
public class BorrowService extends ServiceImpl<BorrowMapper, Borrow> implements IService<Borrow> {
    @Resource
    private OrderService orderService;
    @Resource
    private HostHolder hostHolder;

    /**
     * 借伞
     */
    public boolean borrowUmbrella(Borrow borrow) {
        //设置当前登陆者的id
        borrow.setUserId(hostHolder.getLoginUserId());
        //查询用户是否有未归还记录
        if (getNotReturnBorrowByUserId(borrow.getUserId()).isPresent()) {
            throw GlobalException.causeBy(ResultType.NOT_RETURN);
        }
        //查询用户是否有未支付订单
        if (orderService.getOrderByUserId(borrow.getUserId()).isPresent()) {
            throw GlobalException.causeBy(ResultType.NOT_PAY);
        }
        borrow.setReturned(false);
        return save(borrow);
    }

    /**
     * 还伞
     *
     * @param umbrellaId 雨伞id （任何人都可以归还）
     */
    @Transactional(rollbackFor = GlobalException.class)
    public boolean returnUmbrella(long umbrellaId) {
        final var result = new AtomicBoolean(false);
        getNotReturnBorrowByUmbrellaId(umbrellaId).ifPresent(borrow -> {
            //更改租借状态
            borrow.setReturned(true)
                    .setReturnTime(LocalDateTime.now());
            //生成订单信息,更新租借信息
            result.set(orderService.createOrder(borrow) && updateById(borrow));
        });
        return result.get();
    }

    /**
     * 获取未归还租借信息
     *
     * @param umbrellaId 雨伞id
     */
    public Optional<Borrow> getNotReturnBorrowByUmbrellaId(long umbrellaId) {
        return lambdaQuery().eq(Borrow::getUmbrellaId, umbrellaId)
                .eq(Borrow::getReturned, false)
                .oneOpt();
    }

    /**
     * 获取未归还租借信息
     *
     * @param userId 用户id
     */
    public Optional<Borrow> getNotReturnBorrowByUserId(long userId) {
        return lambdaQuery().eq(Borrow::getUserId, userId)
                .eq(Borrow::getReturned, false)
                .oneOpt();
    }
}
