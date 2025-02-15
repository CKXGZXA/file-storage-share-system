package top.ckxgzxa.filestoragesharesystem.domain.vo;

import lombok.Data;
import top.ckxgzxa.filestoragesharesystem.domain.vo.listitem.UserListItem;

import java.util.List;

/**
 * @author 赵希奥
 * @date 2023/5/30 20:51
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */
@Data
public class UserListResult {
    private List<UserListItem> items;
    private Long total;

    public UserListResult(List<UserListItem> userList, long total) {
        this.items = userList;
        this.total = total;
    }
}
