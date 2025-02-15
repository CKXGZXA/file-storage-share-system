package top.ckxgzxa.filestoragesharesystem.service;

import org.chainmaker.sdk.User;
import org.chainmaker.sdk.crypto.ChainMakerCryptoSuiteException;
import top.ckxgzxa.filestoragesharesystem.domain.po.FilePO;

/**
 * @author 赵希奥
 * @date 2023/4/28 2:52
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description: 用于与长安链chainmaker交互
 */
public interface CMService {

    /**
     * 保存文件信息到长安链
     * @param userId
     * @param file
     * @return 返回的是成功返回的txId
     * @throws ChainMakerCryptoSuiteException
     */
    String saveFileInfo(Long userId, FilePO file, byte[] key) throws ChainMakerCryptoSuiteException;

    /**
     * 根据用户id生成长安链客户端用户
     * @param userId
     * @return
     */
    User getCMUserByUserId(Long userId) throws ChainMakerCryptoSuiteException;

    /**
     * 根据用户id获取用户的地址
     * @param receiverId
     * @return
     */
    String getAddrByUserId(Long receiverId);
}
