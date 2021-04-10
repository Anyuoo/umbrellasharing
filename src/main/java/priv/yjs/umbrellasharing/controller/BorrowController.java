package priv.yjs.umbrellasharing.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import priv.yjs.umbrellasharing.common.CommonResult;
import priv.yjs.umbrellasharing.service.BorrowService;

import javax.annotation.Resource;

/**
 * @author Anyu
 * @since 2021/4/7
 */
@Api(tags = "租借模块")
@RestController
public class BorrowController {
    @Resource
    private BorrowService borrowService;

    @ApiOperation("借伞")
    @PostMapping("/borrow/{umbrellaId}")
    public CommonResult<Void> borrowUmbrella(@PathVariable Long umbrellaId) {
        return CommonResult.handleFuncForBool(borrowService.borrowUmbrella(umbrellaId));
    }

    @ApiOperation("还伞")
    @PostMapping("/return/{umbrellaId}")
    public CommonResult<Void>  returnUmbrella(@PathVariable Long umbrellaId) {
        return CommonResult.handleFuncForBool(borrowService.returnUmbrella(umbrellaId));
    }
}
