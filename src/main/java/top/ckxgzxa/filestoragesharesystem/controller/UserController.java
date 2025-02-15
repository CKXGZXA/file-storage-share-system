package top.ckxgzxa.filestoragesharesystem.controller;

import cn.hutool.core.convert.NumberWithFormat;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import org.springframework.web.bind.annotation.*;
import top.ckxgzxa.filestoragesharesystem.common.model.Result;
import top.ckxgzxa.filestoragesharesystem.domain.dto.ResetPassParams;
import top.ckxgzxa.filestoragesharesystem.domain.dto.UserLoginDTO;
import top.ckxgzxa.filestoragesharesystem.domain.po.UserPO;
import top.ckxgzxa.filestoragesharesystem.service.UserService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 赵希奥
 * @date 2023/3/16 4:20
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description: 用户控制器
 */

@RestController
@RequestMapping("")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 注册用户
     * @param userLoginDTO 用户注册信息
     * @return Result
     */
    @PostMapping("/register")
    @ResponseBody
    public Result register(@RequestBody UserLoginDTO userLoginDTO) {
        UserPO user = new UserPO();
        user.setUsername(userLoginDTO.getUsername());
        user.setPassword(userLoginDTO.getPassword());
        return userService.register(user);
    }

    /**
     * 用户登录
     * @param user 用户登录信息
     * @return Result
     */
    @PostMapping("/login")
    @ResponseBody
    public Result login(@RequestBody UserLoginDTO user) {
        return userService.login(user);
    }

    @PutMapping("/resetPassword")
    @ResponseBody
    public Result resetPassword(@RequestBody ResetPassParams resetParams) {
        return userService.resetPassword(resetParams);
    }

    /**
     * 获取用户信息(From token)
     * @param token 用户token
     * @return Result
     */
    @GetMapping("/getUserInfo")
    @ResponseBody
    public Result getUserInfo(@RequestHeader("Authorization") String token) {
        // 解析token
        JWT jwt = JWTUtil.parseToken(token);

        // 构建返回结果
        Map<String, Object> result = new HashMap(4) {
            {
                put("userId", jwt.getPayload("userId"));
                put("username", jwt.getPayload("username"));
                put("realName", jwt.getPayload("realName"));
                put("roles", jwt.getPayload("roles"));
            }
        };

        return Result.success("获取用户信息成功", result);

    }

    /**
     * 获取用户信息
     * @param token 用户token
     * @return Result
     */
    @GetMapping("/account/getAccountInfo")
    public Result getProfileInfo(@RequestHeader("Authorization") String token) {
        // 解析token
        JWT jwt = JWTUtil.parseToken(token);

        // 拿到userId
        long userId = ((NumberWithFormat) jwt.getPayload("userId")).longValue();

        return userService.getAccountInfo(userId);
    }

    /**
     * 用户登出
     * @param token 用户token
     * @return Result
     */
    @GetMapping("/logout")
    @ResponseBody
    public Result logout(@RequestHeader("Authorization") String token) {
        return userService.logout(token);
    }

    /**
     * 获取用户选项
     * @param key 关键字
     * @return Result
     */
    @GetMapping("/user_options")
    @ResponseBody
    public Result getUserOptions(@RequestParam("key") String key) {
        return userService.getUserOptions(key);
    }
}
