package top.ckxgzxa.filestoragesharesystem.mapper;

import org.apache.ibatis.annotations.*;
import top.ckxgzxa.filestoragesharesystem.domain.po.FilePO;
import top.ckxgzxa.filestoragesharesystem.domain.vo.listitem.FileListItem;

import java.util.List;

/**
 * @author 赵希奥
 * @date 2023/4/7 10:17
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */
public interface FileMapper {

    /**
     * 根据cid找文件
     * @param cid
     * @return
     */
    @Select("select id, file_name as fileName, cid from sys_file where cid=#{cid}")
    FilePO findSFileByCID(String cid);

    /**
     * 插入文件
     * @param file 文件
     * @return 影响行数
     */
    int insertSFile(FilePO file);

    /**
     * 根据md5查询文件
     * @param md5 文件md5
     * @return 文件
     */
    FilePO selectFileByMd5(String md5);

    /**
     * 查询所有文件
     * @return 文件列表
     */
    List<FilePO> selectFileList();

    /**
     * 根据用户id分页查询文件
     * @param userId
     * @return
     */
    @Select("SELECT id, uuid, file_name, size, upload_time, tx_id FROM sys_file WHERE owner = #{userId} AND deleted = 0 ORDER BY id DESC")
    @Results(id = "fileListResultMap", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "uuid", property = "uuid"),
            @Result(column = "file_name", property = "filename"),
            @Result(column = "size", property = "size"),
            @Result(column = "upload_time", property = "uploadTime"),
            @Result(column = "tx_id", property = "txId")
    })
    List<FileListItem> selectFileListByUserId(@Param("userId") Long userId);

    /**
     * 根据文件id查询文件uuid
     * @param fileId
     * @return
     */
    @Select("SELECT uuid FROM sys_file WHERE id = #{fileId} AND deleted = 0")
    String selectUuidByFileId(Long fileId);

    /**
     * 根据文件id查询文件
     * @param fileId 文件id
     * @return
     */
    @Select("SELECT * FROM sys_file WHERE id = #{fileId} AND deleted = 0")
    @Results(id = "filePO",
            value = {@Result(column = "file_name", property = "fileName"),
                    @Result(column = "upload_time", property = "uploadTime"),
                    @Result(column = "owner", property = "ownerId"),
                    @Result(column = "tx_id", property = "txId")}
    )
    FilePO selectFileById(Long fileId);

    /**
     * 根据文件id查询文件所有者
     * @param fileId 文件id
     * @return
     */
    @Select("SELECT owner FROM sys_file WHERE id = #{fileId} AND deleted = 0")
    Long selectOwnerIdById(Long fileId);

    /**
     * 根据文件id删除文件
     * @param fileId 文件id
     * @return
     */
    @Update("UPDATE sys_file SET deleted = 1 WHERE id = #{fileId}")
    int deleteFileById(Long fileId);
}
