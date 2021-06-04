package priv.yjs.umbrellasharing.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import priv.yjs.umbrellasharing.common.CommonResult;
import priv.yjs.umbrellasharing.common.HostHolder;
import priv.yjs.umbrellasharing.model.entity.Order;
import priv.yjs.umbrellasharing.model.param.OrderCondition;
import priv.yjs.umbrellasharing.model.vo.OrderVo;
import priv.yjs.umbrellasharing.service.OrderService;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("orders")
@Api(tags = "订单模块")
public class OrderController {

    @Resource
    private OrderService orderService;
    @Resource
    private HostHolder hostHolder;

    @GetMapping("/lu/all")
    @ApiOperation("查询登录用户订单")
    public CommonResult<List<OrderVo>> getLoginUserOrders() {
        return CommonResult.handleResult(orderService.listAllOrders(hostHolder.getValidLUId()));
    }

    @PostMapping("/lu/condition")
    @ApiOperation("查询登录用户订单")
    public CommonResult<List<OrderVo>> getLoginUserOrdersWith(@RequestBody OrderCondition condition) {
        return CommonResult.handleResult(orderService.listOrders(hostHolder.getValidLUId(),condition));
    }

}
