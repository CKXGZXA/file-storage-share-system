package top.ckxgzxa.filestoragesharesystem.domain.vo;

import lombok.Data;
import top.ckxgzxa.filestoragesharesystem.domain.vo.listitem.ShareToListItem;

import java.util.List;

/**
 * @author 赵希奥
 * @date 2023/5/24 4:13
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description: 分享给我的文件列表分页查询结果
 */

@Data
public class ShareToListResult {

    private List<ShareToListItem> items;

    private Long total;

    public ShareToListResult(List<ShareToListItem> shareToList, long total) {
        this.items = shareToList;
        this.total = total;
    }

}
