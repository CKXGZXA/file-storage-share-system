package top.ckxgzxa.filestoragesharesystem.domain.po;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author 赵希奥
 * @date 2023/3/16 4:16
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description: 用户实体类
 */

@Data
public class UserPO {

    private Long id;
    // private Long orgId;
    private String username;
    private String password;
    private String salt;
    private String token;
    private String addr;
    private String userKey;
    private String userCrt;
    private String userSignKey;
    private String userSignCrt;
    private Integer isAdmin;
    private Integer status;
    private Timestamp createTime;
}