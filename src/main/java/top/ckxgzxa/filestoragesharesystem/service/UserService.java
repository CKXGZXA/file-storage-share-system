package top.ckxgzxa.filestoragesharesystem.service;

import top.ckxgzxa.filestoragesharesystem.common.model.Result;
import top.ckxgzxa.filestoragesharesystem.domain.dto.ResetPassParams;
import top.ckxgzxa.filestoragesharesystem.domain.dto.UserLoginDTO;
import top.ckxgzxa.filestoragesharesystem.domain.po.UserPO;


public interface UserService {

    /**
     * 注册一个用户, 返回是否成功
     * @param user
     * @return
     */
    Result register(UserPO user);

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    Result<Object> login(UserLoginDTO userLoginDTO);

    /**
     * 用户登出
     * @param token
     * @return
     */
    Result logout(String token);

    /**
     * 模糊查询用户选择列表
     * @param key
     * @return
     */
    Result getUserOptions(String key);

    /**
     * 获取用户信息
     * @param userId 用户id
     * @return
     */
    Result getAccountInfo(Long userId);

    /**
     * 重置密码
     * @param resetParams 重置密码参数
     * @return Result
     */
    Result resetPassword(ResetPassParams resetParams);
}
