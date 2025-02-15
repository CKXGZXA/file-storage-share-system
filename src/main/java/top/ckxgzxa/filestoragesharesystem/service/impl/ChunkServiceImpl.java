package top.ckxgzxa.filestoragesharesystem.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.ckxgzxa.filestoragesharesystem.domain.po.ChunkPO;
import top.ckxgzxa.filestoragesharesystem.mapper.ChunkMapper;
import top.ckxgzxa.filestoragesharesystem.service.ChunkService;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 赵希奥
 * @date 2023/4/23 1:03
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */
@Service
public class ChunkServiceImpl implements ChunkService {

    @Resource
    private ChunkMapper chunkMapper;

    @Override
    public Integer saveChunk(MultipartFile chunk, String md5, Integer index, Long chunkSize, String resultFilename) {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(resultFilename, "rw")) {
            // 偏移量
            long offset = (index - 1) * chunkSize;
            // 定位到该分片的偏移量
            randomAccessFile.seek(offset);
            // 写入分片数据
            randomAccessFile.write(chunk.getBytes());

            ChunkPO chunkPO = new ChunkPO(md5, index);
            return chunkMapper.insertChunk(chunkPO);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<Integer> selectChunkByMd5(String md5) {
        List<ChunkPO> chunkPOList = chunkMapper.selectChunkListByMd5(md5);
        List<Integer> indexList = new ArrayList<>();
        for (ChunkPO chunkPO : chunkPOList) {
            indexList.add(chunkPO.getIndex());
        }
        return indexList;
    }

    @Override
    public void deleteChunkByMd5(String md5) {
        chunkMapper.deleteChunkByMd5(md5);
    }

    @Override
    public byte[] getChunk(Integer index, Integer chunkSize, String resultFileName, long offset) {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(resultFileName, "r")) {
            // 定位到该分片的偏移量
            randomAccessFile.seek(offset);
            // 读取
            byte[] buffer = new byte[chunkSize];
            randomAccessFile.read(buffer);
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
