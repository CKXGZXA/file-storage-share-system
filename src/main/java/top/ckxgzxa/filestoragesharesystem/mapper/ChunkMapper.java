package top.ckxgzxa.filestoragesharesystem.mapper;

import top.ckxgzxa.filestoragesharesystem.domain.po.ChunkPO;

import java.util.List;

/**
 * @author 赵希奥
 * @date 2023/4/22 22:23
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */
@Deprecated
public interface ChunkMapper {
    List<ChunkPO> selectChunkListByMd5(String md5);

    Integer insertChunk(ChunkPO chunkPO);

    void deleteChunkByMd5(String md5);
}
