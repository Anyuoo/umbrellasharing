package priv.yjs.umbrellasharing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import priv.yjs.umbrellasharing.common.CommonConst;
import priv.yjs.umbrellasharing.common.ResultType;
import priv.yjs.umbrellasharing.exception.GlobalException;
import priv.yjs.umbrellasharing.mapper.OrderMapper;
import priv.yjs.umbrellasharing.model.entity.Borrow;
import priv.yjs.umbrellasharing.model.entity.Order;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

/**
 * 订单服务层
 *
 * @author Anyu
 * @since 2021/4/8
 */
@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> implements IService<Order> {
    @Resource
    private UmbrellaService umbrellaService;

    /**
     * 生成订单
     *
     * @param borrow 租借信息
     */
    public boolean createOrder(Borrow borrow) {
        if (Objects.isNull(borrow)) {
            return false;
        }
        //查询雨伞信息
        final var umbrella = umbrellaService.getById(borrow.getUmbrellaId()).orElseThrow(() -> GlobalException.causeBy(ResultType.SYSTEM_ERROR));
        if (Objects.isNull(umbrella)) {
            return false;
        }

        //初始化订单信息
        final var order = new Order()
                .setPayerId(borrow.getUserId())
                .setHasPaid(false)
                .setPayee(CommonConst.PAYEE_NAME);
        //计算价格
        calculateTotalPrice(borrow.getBorrowTime(), borrow.getReturnTime(), umbrella.getPrice()).ifPresent(order::setTotalPrice);
        return save(order);
    }

    /**
     * 根据用户id查询订单信息
     *
     * @param userId 支付者id
     */
    public Optional<Order> getOrderByUserId(long userId) {
        return lambdaQuery().eq(Order::getPayerId, userId).eq(Order::getHasPaid, false).oneOpt();
    }

    /**
     * 租借费用计算
     *
     * @param borrowTime 租借时间
     * @param returnTime 归还时间
     * @param price      雨伞价格
     * @return 总价
     */
    private Optional<BigDecimal> calculateTotalPrice(LocalDateTime borrowTime, LocalDateTime returnTime, BigDecimal price) {
        if (Objects.isNull(returnTime) || Objects.isNull(borrowTime) || Objects.isNull(price)) {
            throw GlobalException.causeBy(ResultType.SYS_ARGS_ERROR);
        }
        //时间错误
        if (returnTime.isBefore(borrowTime)) {
            throw GlobalException.causeBy(ResultType.SYS_ARGS_ERROR);
        }

        //计算租借时间
        long minutes = ChronoUnit.MINUTES.between(borrowTime, returnTime);
        //超时时间
        long overtimeMinutes = minutes - CommonConst.BORROW_MINUTE;
        //未超时
        if (overtimeMinutes <= 0) {
            return Optional.of(price);
        }
        //超时
        long hours = overtimeMinutes > 60 ? overtimeMinutes / 60 : 1;
        var overtimePay = hours * CommonConst.BORROW_EXPIRE_PRICE;
        return Optional.of(price.add(new BigDecimal(overtimePay)));
    }

    /**
     * 更新订单状态
     *
     * @param userId 用户id
     */
    public boolean updateOrderPayStatus(long userId) {
        var order = getOrderByUserId(userId).orElseThrow(() -> GlobalException.causeBy(ResultType.ORDER_NOT_EXIST));
        order.setHasPaid(true);
        return updateById(order);
    }

}

