package top.ckxgzxa.filestoragesharesystem.domain.po;

import lombok.Data;

/**
 * @author 赵希奥
 * @date 2023/4/14 13:35
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */

@Data
@Deprecated
public class Organization {

    private Long id;
    private String orgName;
    private String caCrt;
    private String caKey;
}
