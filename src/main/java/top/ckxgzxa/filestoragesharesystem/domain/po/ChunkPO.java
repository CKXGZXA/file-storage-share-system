package top.ckxgzxa.filestoragesharesystem.domain.po;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 赵希奥
 * @date 2023/4/22 22:00
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */

@Data
@Deprecated
@NoArgsConstructor
public class ChunkPO {

    private Long id;
    private String md5;
    private Integer index;

    public ChunkPO(String md5, Integer index) {
        this.md5 = md5;
        this.index = index;
    }
}
