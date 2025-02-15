package top.ckxgzxa.filestoragesharesystem.mapper;

import org.apache.ibatis.annotations.*;
import top.ckxgzxa.filestoragesharesystem.domain.po.UserPO;
import top.ckxgzxa.filestoragesharesystem.domain.vo.UserInfo;
import top.ckxgzxa.filestoragesharesystem.domain.vo.UserOption;
import top.ckxgzxa.filestoragesharesystem.domain.vo.listitem.UserListItem;

import java.util.List;

/**
 * @author 赵希奥
 * @date 2023/3/17 1:57
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */

public interface UserMapper {

    /**
     * 根据用户名查询用户，并返回一个User对象，如果没有找到则返回null
     * @param username
     * @return
     */
    UserPO findUserByUsername(String username);

    /**
     * 插入用户
     * @param user
     * @return
     */
    @Insert("insert into sys_user(username, password, salt, addr, user_key, user_crt, user_sign_key, user_sign_crt, create_time) " +
            "values(#{username}, #{password}, #{salt},#{addr}, #{userKey}, #{userCrt}, #{userSignKey}, #{userSignCrt}, #{createTime})")
    int insertUser(UserPO user);

    /**
     * 根据用户id查询用户
     * @param userId
     * @return
     */
    UserPO selectUserByUserId(Long userId);

    /**
     * 根据用户id更新用户token
     * @param id
     * @param token
     * @return
     */
    @Update("update sys_user set token = #{token} where id = #{id}")
    int updateTokenById(Long id, String token);

    /**
     * 根据用户id删除token
     * @param id
     * @return
     */
    @Update("update sys_user set token = null where id = #{id}")
    int deleteTokenById(Long id);

    /**
     * 获取用户选择项, 用户名模糊
     * @param key
     * @return
     */
    List<UserOption> selectUserOptions(String key);

    /**
     * 获取用户列表(user-management)
     * @return 用户列表
     */
    @Select("SELECT id, username, addr, create_time, user_crt, status FROM sys_user")
    @Results(id = "userListResultMap", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "username", property = "username"),
            @Result(column = "addr", property = "userAddr"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "user_crt", property = "userCrt"),
            @Result(column = "status", property = "status")
    })
    List<UserListItem> selectUserList();

    /**
     * 根据用户id冻结用户
     * @param userId 用户id
     * @return 返回影响的行数
     */
    @Update("update sys_user set status = 0 where id = #{userId}")
    void freezeUser(Long userId);

    /**
     * 根据用户id解冻用户
     * @param userId 用户id
     * @return 返回影响的行数
     */
    @Update("update sys_user set status = 1 where id = #{userId}")
    void unfreezeUser(Long userId);

    /**
     * 根据用户id获取个人信息
     * @param userId 用户id
     * @return
     */
    @Select("SELECT username, addr, user_crt, create_time FROM sys_user WHERE id = #{userId}")
    @Results(id = "userInfoResultMap", value = {
            @Result(column = "username", property = "username"),
            @Result(column = "addr", property = "userAddr"),
            @Result(column = "user_crt", property = "cert"),
            @Result(column = "create_time", property = "createTime")
    })
    UserInfo selectAccountInfo(Long userId);

    /**
     * 根据用户id更新用户密码
     * @param id 用户id
     * @param password 新密码
     * @param salt 盐
     * @return
     */
    @Update("update sys_user set password = #{password}, salt = #{salt} where id = #{id}")
    int updatePassword(Long id, String password, String salt);
}
