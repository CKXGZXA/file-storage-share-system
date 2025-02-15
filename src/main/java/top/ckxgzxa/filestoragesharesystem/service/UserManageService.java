package top.ckxgzxa.filestoragesharesystem.service;

import top.ckxgzxa.filestoragesharesystem.common.model.Result;

/**
 * @author 赵希奥
 * @date 2023/5/30 20:54
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */
public interface UserManageService {

    /**
     * 冻结用户
     * @param userId  用户id
     * @param adminId 管理员id
     * @return Result
     */
    Result freezeUser(Long userId, Long adminId);

    /**
     * 解冻用户
     * @param userId 用户id
     * @param adminId 管理员id
     * @return Result
     */
    Result unfreezeUser(Long userId, Long adminId);

    /**
     * 分页获取用户列表
     * @param page 页码
     * @param pageSize 每页大小
     * @return Result
     */
    Result getUserListPage(Integer page, Integer pageSize);
}
