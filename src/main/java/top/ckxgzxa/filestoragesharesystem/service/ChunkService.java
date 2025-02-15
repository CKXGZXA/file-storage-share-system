package top.ckxgzxa.filestoragesharesystem.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 赵希奥
 * @date 2023/4/23 0:28
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */
public interface ChunkService {

    /**
     * 保存分片
     * @param chunk 分片
     * @param md5 文件md5
     * @param index 分片索引
     * @param chunkSize 分片大小
     * @param resultFilename 文件名
     * @return
     */
    Integer saveChunk(MultipartFile chunk, String md5, Integer index, Long chunkSize, String resultFilename);

    /**
     * 根据md5查询分片
     * @param md5 文件md5
     * @return
     */
    List<Integer> selectChunkByMd5(String md5);

    /**
     * 根据md5删除分片
     * @param md5 文件md5
     */
    void deleteChunkByMd5(String md5);

    /**
     * 获取分片
     * @param index 分片索引
     * @param chunkSize 分片大小
     * @param resultFileName 文件名
     * @param offset 偏移量
     * @return
     */
    byte[] getChunk(Integer index, Integer chunkSize, String resultFileName,long offset);

}
