package top.ckxgzxa.filestoragesharesystem.domain.vo;

import lombok.Data;

/**
 * @author 赵希奥
 * @date 2023/5/31 4:35
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */

@Data
public class UserInfo {
    private String username;
    private String userAddr;
    private String cert;
    private String createTime;
}
