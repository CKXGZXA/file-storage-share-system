package top.ckxgzxa.filestoragesharesystem.domain.vo.listitem;

import lombok.Data;

/**
 * @author 赵希奥
 * @date 2023/5/19 16:47
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description: 文件列表项
 *     {
 *       title: '文件编号',
 *       dataIndex: 'id',
 *       defaultHidden: true,
 *     },
 *     {
 *       title: '文件名',
 *       dataIndex: 'filename',
 *     },
 *     {
 *       title: '文件UUID',
 *       dataIndex: 'uuid',
 *     },
 *     {
 *       title: '文件大小',
 *       width: 100,
 *       dataIndex: 'size',
 *     },
 *     {
 *       title: '上传时间',
 *       dataIndex: 'uploadTime',
 *     },
 */

@Data
public class FileListItem {
    private Long id;
    private String filename;
    private String uuid;
    private Long size;
    private String uploadTime;
    private String txId;

}
