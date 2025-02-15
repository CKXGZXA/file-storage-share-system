package top.ckxgzxa.filestoragesharesystem.domain.dto;

import lombok.Data;

/**
 * @author 赵希奥
 * @date 2023/5/25 5:37
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description: 用于接收长安链文件权限查询结果
 */

@Data
public class CheckAccessResult {

    private String fileId;
    private String userAddr;
    private Integer access;

}

