package top.ckxgzxa.filestoragesharesystem.domain.dto;

import lombok.Data;

/**
 * @author 赵希奥
 * @date 2023/5/22 15:47
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description: 用于接收长安链密钥查询结果
 */

@Data
public class AESResult {

    private String md5;
    private byte[] aesKey;

}
