package top.ckxgzxa.filestoragesharesystem.domain.vo.listitem;

import lombok.Data;

/**
 * @author 赵希奥
 * @date 2023/5/24 3:28
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description: 我分享的文件列表项
 * export interface ShareFromListItem {
 *   uuid: string;
 *   filename: string;
 *   receiverId: number;
 *   receiverName: string;
 *   receiverAddr: string;
 *   shareTime: string;
 * }
 */

@Data
public class ShareFromListItem {

    private Long fileId;
    private String uuid;
    private String filename;
    private Long size;
    private Long receiverId;
    private String receiverName;
    private String receiverAddr;
    private String shareTime;
    private String txId;

}
