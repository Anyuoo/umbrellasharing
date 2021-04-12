package priv.yjs.umbrellasharing.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import priv.yjs.umbrellasharing.common.CommonResult;
import priv.yjs.umbrellasharing.model.param.UserRegisterInput;
import priv.yjs.umbrellasharing.service.UserService;

@Api(tags = "用户模块")
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {

    private final UserService userService;

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public CommonResult<Void> register(@Validated UserRegisterInput input) {
        return CommonResult.applyByBool(userService.register(input.toEntity()));
    }
}
