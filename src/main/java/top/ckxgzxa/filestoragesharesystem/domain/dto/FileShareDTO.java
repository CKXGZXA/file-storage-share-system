package top.ckxgzxa.filestoragesharesystem.domain.dto;

import lombok.Data;

/**
 * @author 赵希奥
 * @date 2023/5/24 2:50
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description: 接收前端文件分享参数
 */

@Data
public class FileShareDTO {

    private Long senderId;
    private Long receiverId;
    private Long fileId;

}
