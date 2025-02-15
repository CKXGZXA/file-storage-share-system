package top.ckxgzxa.filestoragesharesystem.domain.vo;

import lombok.Data;
import top.ckxgzxa.filestoragesharesystem.domain.vo.listitem.FileListItem;

import java.util.List;

/**
 * @author 赵希奥
 * @date 2023/5/20 0:46
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description: 文件列表分页查询结果
 */

@Data
public class FileListResult {

    private List<FileListItem> items;

    private Long total;

    public FileListResult(List<FileListItem> fileList, long total) {
        this.items = fileList;
        this.total = total;
    }
}
