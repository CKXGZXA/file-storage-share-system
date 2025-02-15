package top.ckxgzxa.filestoragesharesystem.controller;

import org.springframework.web.bind.annotation.*;
import top.ckxgzxa.filestoragesharesystem.common.model.Result;
import top.ckxgzxa.filestoragesharesystem.common.utils.JwtUtils;
import top.ckxgzxa.filestoragesharesystem.service.UserManageService;

import javax.annotation.Resource;

/**
 * @author 赵希奥
 * @date 2023/5/30 4:54
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */

@RestController
@RequestMapping("/user-manage")
public class UserManageController {

    @Resource
    private UserManageService userManageService;

    /**
     * 冻结用户
     * @param userId 用户id
     * @param token token
     * @return
     */
    @PutMapping("/freeze/{userId}")
    @ResponseBody
    public Result freezeUser(@PathVariable Long userId, @RequestHeader("Authorization") String token) {
        Long adminId = JwtUtils.getUserIdFromJwt(token);
        return userManageService.freezeUser(userId, adminId);
    }

    /**
     * 解冻用户
     * @param userId 用户id
     * @param token token
     * @return
     */
    @PutMapping("/unfreeze/{userId}")
    @ResponseBody
    public Result unfreezeUser(@PathVariable Long userId, @RequestHeader("Authorization") String token) {
        Long adminId = JwtUtils.getUserIdFromJwt(token);
        return userManageService.unfreezeUser(userId, adminId);
    }

    @GetMapping("/getUserList")
    @ResponseBody
    public Result getUserListPage(@RequestParam("page") Integer page,
                                  @RequestParam("pageSize") Integer pageSize) {
        return userManageService.getUserListPage(page, pageSize);
    }
}
