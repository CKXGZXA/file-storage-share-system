package top.ckxgzxa.filestoragesharesystem.domain.vo;

import lombok.Data;
import top.ckxgzxa.filestoragesharesystem.domain.vo.listitem.ShareFromListItem;

import java.util.List;

/**
 * @author 赵希奥
 * @date 2023/5/24 4:11
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description: 我分享的文件列表分页查询结果
 */

@Data
public class ShareFromListResult {

    private List<ShareFromListItem> items;

    private Long total;

    public ShareFromListResult(List<ShareFromListItem> shareFromList, long total) {
        this.items = shareFromList;
        this.total = total;
    }

}
