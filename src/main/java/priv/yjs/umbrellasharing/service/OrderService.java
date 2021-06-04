package priv.yjs.umbrellasharing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import priv.yjs.umbrellasharing.common.CommonConst;
import priv.yjs.umbrellasharing.common.CommonResult;
import priv.yjs.umbrellasharing.common.ResultType;
import priv.yjs.umbrellasharing.exception.GlobalException;
import priv.yjs.umbrellasharing.mapper.OrderMapper;
import priv.yjs.umbrellasharing.model.entity.Borrow;
import priv.yjs.umbrellasharing.model.entity.Order;
import priv.yjs.umbrellasharing.model.param.OrderCondition;
import priv.yjs.umbrellasharing.model.vo.OrderVo;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Resource
    private BorrowService borrowService;

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
                .setBrwId(borrow.getId())
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
    public Optional<Order> getUnpaidOrderByUserId(long userId) {
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
        var order = getUnpaidOrderByUserId(userId).orElseThrow(() -> GlobalException.causeBy(ResultType.ORDER_NOT_EXIST));
        order.setHasPaid(true);
        return updateById(order);
    }

    public List<OrderVo> listAllOrders(long validLUId) {
        List<Order> orders = lambdaQuery().eq(Order::getPayerId, validLUId).orderByDesc(Order::getCreateTime).list();
        return convertPo2Vo(orders);
    }

    public List<OrderVo> listOrders(long validLUId, OrderCondition condition) {
        List<Order> orders = lambdaQuery().eq(Order::getPayerId, validLUId).ge(condition.getBeginTime() != null, Order::getCreateTime, condition.getBeginTime()).orderByDesc(Order::getCreateTime).list();
        List<OrderVo> orderVos = convertPo2Vo(orders);
        if (StringUtils.isNotBlank(condition.getPm())) {
          return   orderVos.stream().filter(orderVo -> orderVo.getBorrowPm().equals(condition.getPm())).collect(Collectors.toUnmodifiableList());
        }
        return orderVos;
    }

    private List<OrderVo> convertPo2Vo(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return new ArrayList<>();
        }
        return orders.stream().map(order -> {
            OrderVo orderVo = new OrderVo();
            BeanUtils.copyProperties(order, orderVo);
            Borrow borrow = borrowService.getById(order.getBrwId());
            if (borrow != null) {
                orderVo.setPmName(borrow.getBorrowPm());
                orderVo.setUmId(borrow.getUmbrellaId());
                orderVo.setBorrowTime(borrow.getBorrowTime());
                orderVo.setReturnTime(borrow.getReturnTime());
            }
            return orderVo;
        }).collect(Collectors.toUnmodifiableList());
    }
}

