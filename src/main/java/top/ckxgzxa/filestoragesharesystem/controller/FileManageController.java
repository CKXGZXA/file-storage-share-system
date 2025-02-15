package top.ckxgzxa.filestoragesharesystem.controller;

import cn.hutool.core.convert.NumberWithFormat;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import org.springframework.web.bind.annotation.*;
import top.ckxgzxa.filestoragesharesystem.common.model.Result;
import top.ckxgzxa.filestoragesharesystem.domain.dto.DelFileParams;
import top.ckxgzxa.filestoragesharesystem.domain.dto.FileShareDTO;
import top.ckxgzxa.filestoragesharesystem.service.FileManageService;

import javax.annotation.Resource;

/**
 * @author 赵希奥
 * @date 2023/5/19 17:06
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description: 文件管理控制器
 */
@RestController
@RequestMapping("/file-manage")
public class FileManageController {

    @Resource
    private FileManageService fileManageService;

    @GetMapping("/getFileList")
    @ResponseBody
    public Result getFileListPage(@RequestParam("page") Integer page,
                                  @RequestParam("pageSize") Integer pageSize,
                                  @RequestHeader("Authorization") String token) {

        // 解析token
        JWT jwt = JWTUtil.parseToken(token);

        // 拿到userId
        Long userId = ((NumberWithFormat) jwt.getPayload("userId")).longValue();

        return fileManageService.getFileListPageByUserId(page, pageSize, userId);

    }

    /**
     * 分享文件操作
     * @param fileShareParams 分享文件参数
     * @return Result
     */
    @PostMapping("share")
    @ResponseBody
    public Result shareFile(@RequestBody FileShareDTO fileShareParams) {
        return fileManageService.shareFile(fileShareParams.getSenderId(), fileShareParams.getReceiverId(), fileShareParams.getFileId());
    }

    /**
     * 撤销分享操作
     * @param fileId 文件id
     * @param token 用户token
     * @return
     */
    @DeleteMapping("/delFile")
    @ResponseBody
    public Result delFile(@RequestBody DelFileParams delFileParams,
                          @RequestHeader("Authorization") String token) {
        // 解析token
        JWT jwt = JWTUtil.parseToken(token);

        // 拿到userId
        Long userId = ((NumberWithFormat) jwt.getPayload("userId")).longValue();

        return fileManageService.delFile(userId, delFileParams.getFileId());
    }
}
