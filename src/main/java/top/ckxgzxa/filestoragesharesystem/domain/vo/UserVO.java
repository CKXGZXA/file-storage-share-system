package top.ckxgzxa.filestoragesharesystem.domain.vo;

import lombok.Data;

/**
 * @author 赵希奥
 * @date 2023/3/20 3:19
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description: 用户信息VO
 */

@Data
@Deprecated
public class UserVO {
    private Long id;
    private String username;
    private Long orgId;
    private String publicKey;
    private String addr;
}
