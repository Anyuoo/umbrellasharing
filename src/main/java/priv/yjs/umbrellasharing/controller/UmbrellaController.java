package priv.yjs.umbrellasharing.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import priv.yjs.umbrellasharing.annotation.CommonResultHandler;
import priv.yjs.umbrellasharing.common.CommonResult;
import priv.yjs.umbrellasharing.model.entity.Umbrella;
import priv.yjs.umbrellasharing.model.param.UmbrellaInput;
import priv.yjs.umbrellasharing.model.param.UmbrellaPageCondition;
import priv.yjs.umbrellasharing.model.param.UmbrellaUpdateInput;
import priv.yjs.umbrellasharing.model.vo.UmbrellaVo;
import priv.yjs.umbrellasharing.service.UmbrellaService;

import javax.annotation.Resource;

/**
 * @author Anyu
 * @since 2021/4/7
 */
@RestController
@Api(tags = "雨伞模块")
@RequestMapping(path = "/umbrellas")
@CommonResultHandler
public class UmbrellaController {
    @Resource
    private UmbrellaService umbrellaService;

    @ApiOperation("添加雨伞信息")
    @PostMapping
    public CommonResult<Void> saveUmbrellaInfo(@Validated UmbrellaInput input) {
        return CommonResult.applyByBool(umbrellaService.saveUmbrella(input.toEntity()));
    }

    @ApiOperation("修改雨伞信息")
    @PutMapping
    public CommonResult<Void> updateUmbrellaInfo(UmbrellaUpdateInput input) {
        return CommonResult.applyByBool(umbrellaService.updateById(input.toEntity()));
    }

    @ApiOperation("删除雨伞信息")
    @DeleteMapping(path = "/{id}")
    public CommonResult<Void> removeUmbrella(@PathVariable Long id) {
        return CommonResult.applyByBool(umbrellaService.removeById(id));
    }

    @ApiOperation("查询雨伞信息")
    @GetMapping(path = "/idle")
    public CommonResult<Page<UmbrellaVo>> listIdleUmbrellas(@Validated  UmbrellaPageCondition condition) {
        return CommonResult.handleResult(umbrellaService.listIdleUmbrellas(condition));
    }


}
