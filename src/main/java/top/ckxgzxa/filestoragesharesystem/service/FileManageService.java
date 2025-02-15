package top.ckxgzxa.filestoragesharesystem.service;

import top.ckxgzxa.filestoragesharesystem.common.model.Result;

/**
 * @author 赵希奥
 * @date 2023/5/20 1:04
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */
public interface FileManageService {

    /**
     * 根据用户id获取文件列表分页
     * @param page
     * @param pageSize
     * @param userId
     * @return Result
     */
    Result getFileListPageByUserId(Integer page, Integer pageSize, Long userId);

    /**
     * 分享文件
     * @param senderId
     * @param receiverId
     * @param fileId
     * @return Result
     */
    Result shareFile(Long senderId, Long receiverId, Long fileId);

    /**
     * 删除文件
     * @param userId 用户id
     * @param fileId 文件id
     * @return Result
     */
    Result delFile(Long userId, Long fileId);
}
