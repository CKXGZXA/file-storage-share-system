package top.ckxgzxa.filestoragesharesystem.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

import java.sql.Timestamp;

/**
 * @author 赵希奥
 * @date 2023/5/24 0:08
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */
public interface FileShareMapper {

    /**
     * 插入文件分享记录
     * @param senderId
     * @param receiverId
     * @param fileId
     * @param shareTime
     * @param txId
     */
    @Insert("INSERT sys_file_share (user_from,user_to,file_id, share_time, tx_id) VALUES (#{senderId},#{receiverId},#{fileId}, #{shareTime}, #{txId})")
    void insertFileShare(Long senderId, Long receiverId, Long fileId, Timestamp shareTime, String txId);

    /**
     * 删除文件分享记录
     * @param senderId 分享者id
     * @param receiverId 接收者id
     * @param fileId 文件id
     */
    @Delete("DELETE FROM sys_file_share WHERE user_from = #{senderId} AND user_to = #{receiverId} AND file_id = #{fileId}")
    void deleteFileShare(Long senderId, Long receiverId, Long fileId);
}
