package top.ckxgzxa.filestoragesharesystem.controller;

import cn.hutool.core.convert.NumberWithFormat;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import org.springframework.web.bind.annotation.*;
import top.ckxgzxa.filestoragesharesystem.common.model.Result;
import top.ckxgzxa.filestoragesharesystem.domain.dto.FileRevokeDTO;
import top.ckxgzxa.filestoragesharesystem.service.ShareManageService;

import javax.annotation.Resource;

/**
 * @author 赵希奥
 * @date 2023/5/24 3:45
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description: 分享管理控制器, 用于分享文件的管理
 */

@RestController
@RequestMapping("/share-manage")
public class ShareManageController {

    @Resource
    private ShareManageService shareManageService;

    /**
     * 分页获取我分享的文件列表
     * @param page 页码
     * @param pageSize 每页数量
     * @param token 用户token
     * @return
     */
    @GetMapping("/getShareFromList")
    @ResponseBody
    public Result getShareFromListPage(@RequestParam("page") Integer page,
                                       @RequestParam("pageSize") Integer pageSize,
                                       @RequestHeader("Authorization") String token) {
        // 解析token
        JWT jwt = JWTUtil.parseToken(token);

        // 拿到userId
        Long userId = ((NumberWithFormat) jwt.getPayload("userId")).longValue();

        return shareManageService.getFromListPageByUserId(page, pageSize, userId);
    }


    /**
     * 分页获取分享给我的文件列表
     * @param page 页码
     * @param pageSize 每页数量
     * @param token 用户token
     * @return
     */
    @GetMapping("/getShareToList")
    @ResponseBody
    public Result getShareToListPage(@RequestParam("page") Integer page,
                                     @RequestParam("pageSize") Integer pageSize,
                                     @RequestHeader("Authorization") String token) {
        // 解析token
        JWT jwt = JWTUtil.parseToken(token);

        // 拿到userId
        Long userId = ((NumberWithFormat) jwt.getPayload("userId")).longValue();

        return shareManageService.getToListPageByUserId(page, pageSize, userId);
    }

    @PostMapping("/revokeShare")
    public Result revokeShare(@RequestBody FileRevokeDTO fileRevokeParams,
                              @RequestHeader("Authorization") String token) {
        // 解析token
        JWT jwt = JWTUtil.parseToken(token);

        // 拿到userId
        long cmUserId = ((NumberWithFormat) jwt.getPayload("userId")).longValue();

        return shareManageService.revokeShare(cmUserId, fileRevokeParams.getFileId(), fileRevokeParams.getUserId());
    }
}
