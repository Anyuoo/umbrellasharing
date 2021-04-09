package priv.yjs.umbrellasharing.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import priv.yjs.umbrellasharing.annotation.CommonResultHandler;
import priv.yjs.umbrellasharing.common.ResultType;
import priv.yjs.umbrellasharing.model.param.UmbrellaInput;
import priv.yjs.umbrellasharing.model.param.UmbrellaUpdateInput;
import priv.yjs.umbrellasharing.service.UmbrellaService;

import javax.annotation.Resource;

/**
 * @author Anyu
 * @since 2021/4/7
 */
@RestController
@Api(tags = "雨伞API")
@RequestMapping(path = "/umbrellas")
@CommonResultHandler
public class UmbrellaController {
    @Resource
    private UmbrellaService umbrellaService;

    @ApiOperation("添加雨伞信息")
    @PostMapping
    public ResultType saveUmbrellaInfo(@Validated UmbrellaInput input) {
        if (umbrellaService.saveUmbrella(input.toEntity())) {
            return ResultType.SUCCESS;
        }
        return ResultType.FAIL;
    }

    @ApiOperation("修改雨伞信息")
    @PutMapping
    public ResultType updateUmbrellaInfo(UmbrellaUpdateInput input) {
        if (umbrellaService.updateById(input.toEntity())) {
            return ResultType.SUCCESS;
        }
        return ResultType.FAIL;
    }
}
