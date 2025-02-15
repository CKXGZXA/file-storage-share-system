package top.ckxgzxa.filestoragesharesystem.domain.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author 赵希奥
 * @date 2023/4/7 10:07
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description: 文件实体类
 */
// https://ipfs.moralis.io:2053/ipfs/Qmecpc277XMF5ayJYnVmBLPmKt57uMg2R4DZsDddViftwV/1764253469.pdf
@Data
@NoArgsConstructor
public class FilePO {
    private Long id;
    private String uuid;
    private String fileName;
    private String cid;
    private Integer status;
    private Long size;
    private String md5;
    private Timestamp uploadTime;
    private Long ownerId;
    private String txId;
    private byte deleted;

    public FilePO(String fileName, String md5, Long fileSize) {
        this.fileName = fileName;
        this.md5 = md5;
        this.size = fileSize;
    }
}
