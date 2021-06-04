package priv.yjs.umbrellasharing.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import priv.yjs.umbrellasharing.common.CommonResult;
import priv.yjs.umbrellasharing.common.HostHolder;
import priv.yjs.umbrellasharing.model.entity.LoginUser;
import priv.yjs.umbrellasharing.model.entity.User;
import priv.yjs.umbrellasharing.model.param.Avatar;
import priv.yjs.umbrellasharing.model.param.PasswordInput;
import priv.yjs.umbrellasharing.model.param.UserRegisterInput;
import priv.yjs.umbrellasharing.model.param.UserUpdateInput;
import priv.yjs.umbrellasharing.service.UserService;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "用户模块")
@RestController
@RequestMapping("/users")
public class UserController {

    @Resource
    private  UserService userService;
    @Resource
    private HostHolder hostHolder;

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public CommonResult<Void> register(@Validated @RequestBody UserRegisterInput input) {
        return CommonResult.applyByBool(userService.register(input.toEntity()));
    }
    @ApiOperation("获取当前用户信息")
    @GetMapping("/lu-info")
    public CommonResult<User> getLoginUserInfo() {
        return CommonResult.handleResult(userService.getById(hostHolder.getLUId()));
    }

    @ApiOperation("获取所有用户信息")
    @GetMapping("/all")
    public CommonResult<List<User>> listAllUsers(String username,String nickname) {
        return CommonResult.handleResult(userService.listAll(username,nickname));
    }
    @ApiOperation("修改当前用户")
    @PostMapping("/lu-info")
    public CommonResult<Void> updateLoginUser(@Validated @RequestBody UserUpdateInput input) {
        return CommonResult.applyByBool(userService.updateUserInfo(hostHolder.getValidLUId(),input));
    }

    @ApiOperation("修改当前用户密码")
    @PostMapping("/lu-password")
    public CommonResult<Void> updateLoginUserPass(@Validated @RequestBody PasswordInput input) {
        return CommonResult.applyByBool(userService.updatePassword(hostHolder.getValidLUId(), input));
    }

    @ApiOperation("修改当前用户头像")
    @PostMapping("/lu-avatar")
    public CommonResult<Void> updateLoginUserAvatar(@Validated @RequestBody Avatar avatar) {
        return CommonResult.applyByBool(userService.updateAvatar(hostHolder.getValidLUId(), avatar.getAvatar()));
    }


}
