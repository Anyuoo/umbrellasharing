package priv.yjs.umbrellasharing.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import priv.yjs.umbrellasharing.annotation.CommonResultHandler;
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
@CommonResultHandler
public class BorrowController {
    @Resource
    private BorrowService borrowService;

    @ApiOperation("借伞")
    @PostMapping("/borrow")
    public ResultType borrowUmbrella(@Validated BorrowInput input) {
        if (borrowService.borrowUmbrella(input.toEntity())) {
            return ResultType.SUCCESS;
        }
        return ResultType.FAIL;
    }

    @ApiOperation("还伞")
    @PostMapping("/return")
    public ResultType returnUmbrella(long umbrellaId) {
        if (borrowService.returnUmbrella(umbrellaId)) {
            return ResultType.SUCCESS;
        }
        return ResultType.FAIL;
    }
}
