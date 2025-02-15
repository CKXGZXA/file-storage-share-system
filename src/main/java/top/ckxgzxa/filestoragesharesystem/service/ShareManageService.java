package top.ckxgzxa.filestoragesharesystem.service;

import top.ckxgzxa.filestoragesharesystem.common.model.Result;

/**
 * @author 赵希奥
 * @date 2023/5/24 3:52
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */
public interface ShareManageService {
    /**
     * 根据用户id获取我分享的文件列表分页
     * @param page
     * @param pageSize
     * @param userId
     * @return
     */
    Result getFromListPageByUserId(Integer page, Integer pageSize, Long userId);

    /**
     * 根据用户id获取分享给我的文件列表分页
     * @param page
     * @param pageSize
     * @param userId
     * @return
     */
    Result getToListPageByUserId(Integer page, Integer pageSize, Long userId);

    /**
     * 撤销某用户对文件的访问权限
     * @param cmUserId 客户端用户id
     * @param fileId 文件id
     * @param userId 被分享用户id
     * @return
     */
    Result revokeShare(Long cmUserId, Long fileId, Long userId);
}
