package top.ckxgzxa.filestoragesharesystem.domain.dto;

import lombok.Data;

/**
 * @author 赵希奥
 * @date 2023/5/31 15:31
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */
@Data
public class ResetPassParams {
    private String username;
    private String newPassword;
    private String privateKey;
}
