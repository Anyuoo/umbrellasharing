package priv.yjs.umbrellasharing.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import priv.yjs.umbrellasharing.annotation.CommonResultHandler;
import priv.yjs.umbrellasharing.common.CommonResult;
import priv.yjs.umbrellasharing.common.ResultType;
import priv.yjs.umbrellasharing.model.param.BorrowInput;
import priv.yjs.umbrellasharing.service.BorrowService;

import javax.annotation.Resource;

/**
 * @author Anyu
 * @since 2021/4/7
 */
@Api(tags = "借伞API")
@RestController
public class BorrowController {
    @Resource
    private BorrowService borrowService;

    @ApiOperation("借伞")
    @PostMapping("/borrow")
    public CommonResult<Void> borrowUmbrella(@Validated BorrowInput input) {
        return CommonResult.service(borrowService.borrowUmbrella(input.toEntity()));
    }

    @ApiOperation("还伞")
    @PostMapping("/return")
    public CommonResult<Void>  returnUmbrella(long umbrellaId) {
        return CommonResult.service(borrowService.returnUmbrella(umbrellaId));
    }
}
