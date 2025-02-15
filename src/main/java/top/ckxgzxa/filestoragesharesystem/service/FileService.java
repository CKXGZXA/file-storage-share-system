package top.ckxgzxa.filestoragesharesystem.service;

import org.chainmaker.sdk.crypto.ChainMakerCryptoSuiteException;
import org.springframework.web.multipart.MultipartFile;
import top.ckxgzxa.filestoragesharesystem.common.model.Result;
import top.ckxgzxa.filestoragesharesystem.domain.po.FilePO;

import java.util.List;

/**
 * @author 赵希奥
 * @date 2023/4/7 10:22
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */
public interface FileService {

    Result saveFile(FilePO file);

    Result getUrlFromCid(String cid);

    Integer addFile(FilePO filePO);

    Boolean selectFileByMd5(String md5);

    @Deprecated
    List<FilePO> selectFileList();

    /**
     * 上传文件到IPFS, 已经过论证测试, 方法确认无误.
     * @param file
     * @param token
     * @return
     */
    Result  upload(MultipartFile file, String token);

    /**
     * 判断用户是否有权限访问文件
     * @param userId
     * @param fileId
     * @return
     * @throws ChainMakerCryptoSuiteException
     */
    boolean checkFileAccess(Long userId, Long fileId) throws ChainMakerCryptoSuiteException;

    /**
     * 从IPFS下载加密文件, 已经过论证测试, 方法确认无误.
     * @param userId
     * @param fileId
     * @return
     * @throws Exception
     */
    byte[] downloadFromIPFS(Long userId, Long fileId) throws Exception;
}
