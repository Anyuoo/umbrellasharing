package priv.yjs.umbrellasharing.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import priv.yjs.umbrellasharing.annotation.CommonResultHandler;
import priv.yjs.umbrellasharing.common.CommonResult;
import priv.yjs.umbrellasharing.common.ResultType;
import priv.yjs.umbrellasharing.model.entity.Placement;
import priv.yjs.umbrellasharing.model.param.PlacementInput;
import priv.yjs.umbrellasharing.model.param.PlacementUmbrellasInput;
import priv.yjs.umbrellasharing.model.param.PlacementUpdateInput;
import priv.yjs.umbrellasharing.service.PlacementService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Anyu
 * @since 2021/4/8
 */
@Api(tags = "放置点API")
@RestController
@RequestMapping(path = "/placements")
public class PlacementController {
    @Resource
    private PlacementService placementService;

    @ApiOperation("创建放置点")
    @PostMapping
    public CommonResult<Void> createPlacement(@Validated PlacementInput input) {
        return CommonResult.service(placementService.savePlacement(input.toEntity()));
    }

    @ApiOperation("修改放置点")
    @PutMapping
    public CommonResult<Void> updatePlacement(@Validated PlacementUpdateInput input) {
        return CommonResult.service(placementService.updatePlacement(input.toEntity()));
    }

    @ApiOperation("查询所有放置点")
    @GetMapping(path = "/all")
    @CommonResultHandler
    public List<Placement> listAllPlacement() {
        return placementService.list();
    }

    @ApiOperation("添加雨伞")
    @PutMapping("/umbrellas")
    public CommonResult<Void> addUmbrellas(PlacementUmbrellasInput input) {
        return CommonResult.service(placementService.addUmbrellas(input.getPmId(), input.getUmbrellaIds()));
    }
}
