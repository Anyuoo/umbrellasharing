package priv.yjs.umbrellasharing.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import priv.yjs.umbrellasharing.annotation.CommonResultHandler;
import priv.yjs.umbrellasharing.common.CommonResult;
import priv.yjs.umbrellasharing.model.param.LoginInput;
import priv.yjs.umbrellasharing.service.LoginService;

import javax.annotation.Resource;
import java.util.Optional;

@RestController
@CommonResultHandler
@Api(tags = "登录模块")
public class LoginController {
    @Resource
    private LoginService loginService;


    @ApiOperation("用户登录")
    @PostMapping("/login")
    public CommonResult<Optional<String>> login(@Validated LoginInput input) {
        return CommonResult.handleForResult(()->loginService.login(input.getUsername(), input.getPassword()));
    }
}
