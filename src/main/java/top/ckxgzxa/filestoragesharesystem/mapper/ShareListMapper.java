package top.ckxgzxa.filestoragesharesystem.mapper;

import org.apache.ibatis.annotations.Select;
import top.ckxgzxa.filestoragesharesystem.domain.vo.listitem.ShareFromListItem;
import top.ckxgzxa.filestoragesharesystem.domain.vo.listitem.ShareToListItem;

import java.util.List;

/**
 * @author 赵希奥
 * @date 2023/5/24 3:56
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */
public interface ShareListMapper {

    /**
     * 根据用户id获取我分享的文件列表
     * @param userId
     * @return
     */
    @Select("SELECT fileId, uuid, filename, size, receiverId, receiverName, receiverAddr, shareTime, txId FROM share_from_list WHERE senderId = #{userId} ORDER BY shareTime DESC")
    List<ShareFromListItem> selectFromListByUserId(Long userId);

    /**
     * 根据用户id获取分享给我的文件列表
     * @param userId
     * @return
     */
    @Select("SELECT fileId, uuid, filename, size, senderId, senderName, senderAddr, shareTime, txId FROM share_to_list WHERE receiverId = #{userId} ORDER BY shareTime DESC")
    List<ShareToListItem> selectToListByUserId(Long userId);

}
