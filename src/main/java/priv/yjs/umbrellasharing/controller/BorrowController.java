package priv.yjs.umbrellasharing.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import priv.yjs.umbrellasharing.common.CommonResult;
import priv.yjs.umbrellasharing.common.HostHolder;
import priv.yjs.umbrellasharing.model.entity.Borrow;
import priv.yjs.umbrellasharing.model.vo.BorrowVo;
import priv.yjs.umbrellasharing.service.BorrowService;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author Anyu
 * @since 2021/4/7
 */
@Api(tags = "租借模块")
@RestController
public class BorrowController {
    @Resource
    private BorrowService borrowService;
    @Resource
    private HostHolder hostHolder;

    @ApiOperation("借伞")
    @PostMapping("/borrow/{umbrellaId}")
    public CommonResult<Void> borrowUmbrella(@PathVariable Long umbrellaId) {
        return CommonResult.applyByBool(borrowService.borrowUmbrella(umbrellaId));
    }

    @ApiOperation("还伞")
    @PostMapping("/return")
    public CommonResult<Void>  returnUmbrella() {
        return CommonResult.applyByBool(borrowService.returnUmbrella());
    }

    @ApiOperation("当前为归还记录")
    @GetMapping("/borrow/lu")
    public CommonResult<BorrowVo> getLoginUserBorrow() {
        return CommonResult.handleResult(borrowService.getUnReturnLU());
    }

}
