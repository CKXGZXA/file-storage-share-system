package top.ckxgzxa.filestoragesharesystem.domain.vo.listitem;

import lombok.Data;

/**
 * @author 赵希奥
 * @date 2023/5/30 20:41
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */
@Data
public class UserListItem {
    private Long id;
    private String username;
    private String userAddr;
    private String createTime;
    private String userCrt;
    private Integer status;
}
