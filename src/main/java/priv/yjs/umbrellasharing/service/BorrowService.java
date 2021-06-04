package priv.yjs.umbrellasharing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import priv.yjs.umbrellasharing.common.HostHolder;
import priv.yjs.umbrellasharing.common.ResultType;
import priv.yjs.umbrellasharing.exception.GlobalException;
import priv.yjs.umbrellasharing.mapper.BorrowMapper;
import priv.yjs.umbrellasharing.model.entity.*;
import priv.yjs.umbrellasharing.model.vo.BorrowVo;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Optional;

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
    @Resource
    private UmbrellaService umbrellaService;
    @Resource
    private PlacementService placementService;

    /**
     * 借伞
     */
    @Transactional(rollbackFor = Throwable.class)
    public boolean borrowUmbrella(long umbrellaId) {
        //当前登陆者的id
        long loginUserId = hostHolder.getValidLUId();
        //查询用户是否有未归还记录
        if (getNotReturnBorrowByUserId(loginUserId).isPresent()) {
            throw GlobalException.causeBy(ResultType.NOT_RETURN);
        }
        //查询用户是否有未支付订单
        if (orderService.getUnpaidOrderByUserId(loginUserId).isPresent()) {
            throw GlobalException.causeBy(ResultType.NOT_PAY);
        }
       ;
        var borrow = new Borrow()
                .setBorrowPm( placementService.getByUmbrellaId(umbrellaId).map(Placement::getPosition).orElse(""))
                .setUserId(loginUserId)
                .setUmbrellaId(umbrellaId)
                .setReturned(false);
        return  umbrellaService.borrow(umbrellaId) && save(borrow);
    }

    /**
     * 还伞
     *
     *
     */
    @Transactional(rollbackFor = GlobalException.class)
    public boolean returnUmbrella() {
        Borrow brw = getNotReturnBorrowByUserId(hostHolder.getLUId()).orElse(null);
        if (brw != null) {
            //更改租借状态
            brw.setReturned(true)
                    .setReturnTime(LocalDateTime.now());
            //生成订单信息,更新租借信息
            return orderService.createOrder(brw) && updateById(brw) && umbrellaService.returnUmbrella(brw.getUmbrellaId());
        }

        return false;
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

    public BorrowVo getUnReturnLU() {
        BorrowVo borrowVo = new BorrowVo();
        long luId = hostHolder.getLUId();
        getNotReturnBorrowByUserId(luId).ifPresent(b->{
            BeanUtils.copyProperties(b,borrowVo);
            borrowVo.setPmName(placementService.getById(luId).map(Placement::getPosition).orElse(""))
                    .setUsername(hostHolder.getLoginUser()
                            .map(LoginUser::getUser)
                            .map(User::getUsername).orElse(""));
        });
        return borrowVo;
    }
}
