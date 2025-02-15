package top.ckxgzxa.filestoragesharesystem.service.impl;

import org.chainmaker.pb.common.ResultOuterClass;
import org.chainmaker.sdk.User;
import org.chainmaker.sdk.crypto.ChainMakerCryptoSuiteException;
import org.chainmaker.sdk.utils.CryptoUtils;
import org.springframework.stereotype.Service;
import top.ckxgzxa.filestoragesharesystem.cmsdk.Contract;
import top.ckxgzxa.filestoragesharesystem.domain.po.FilePO;
import top.ckxgzxa.filestoragesharesystem.domain.po.UserPO;
import top.ckxgzxa.filestoragesharesystem.mapper.UserMapper;
import top.ckxgzxa.filestoragesharesystem.service.CMService;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.Map;

import static top.ckxgzxa.filestoragesharesystem.cmsdk.InitClient.chainClient;

/**
 * @author 赵希奥
 * @date 2023/4/28 2:56
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */
@Service
public class CMServiceImpl implements CMService {

    @Resource
    private UserMapper userMapper;

    @Override
    public String saveFileInfo(Long userId, FilePO file, byte[] key) throws ChainMakerCryptoSuiteException {

        // 1. 按用户id将用户信息读出来
        UserPO userInfo = userMapper.selectUserByUserId(userId);

        byte[] keyBytes = userInfo.getUserKey().getBytes();
        byte[] crtBytes = userInfo.getUserCrt().getBytes();

        // 2. 根据用户的公钥和私钥信息生成长安链用户
        User userCM = new User("root", keyBytes,
                crtBytes, keyBytes, crtBytes);
        userCM.setEnableTxResultDispatcher(true);
        chainClient.setClientUser(userCM);

        // 3. 拿到用户的地址
        String addr = CryptoUtils.getEVMAddressFromCertBytes(crtBytes);

        // 4. 调用chainmaker的接口将文件信息存入链上
        // 上传参数 userAddr,fileId,fileName,fileStatus,fileHash,fileTime,fileSize
        Map<String, byte[]> params = new HashMap<>();
        params.put("userAddr", addr.getBytes());
        // todo 下行以后应予以修改
        params.put("fileId", file.getUuid().toString().getBytes());
        params.put("fileName", file.getFileName().getBytes());
        params.put("fileStatus", "private".getBytes());
        params.put("fileHash", file.getCid().getBytes());
        params.put("fileTime", String.valueOf(System.currentTimeMillis()).getBytes());
        params.put("fileSize", file.getSize().toString().getBytes());
        params.put("fileKey", key);

        ResultOuterClass.TxResponse responseInfo = Contract.invokeContract(chainClient, "FileManage",
                "addFile", params);

        // 5. 返回交易id
        if (responseInfo != null && 0 == responseInfo.getContractResult().getCode()) {
            return responseInfo.getTxId();
        }

        return null;
    }

    @Override
    public User getCMUserByUserId(Long userId) throws ChainMakerCryptoSuiteException {

        // 1. 读用户信息, 比如公钥, 私钥证书等
        UserPO userInfo = userMapper.selectUserByUserId(userId);

        byte[] keyBytes = userInfo.getUserKey().getBytes();
        byte[] crtBytes = userInfo.getUserCrt().getBytes();

        // 2. 根据用户的公钥和私钥信息生成长安链用户
        User userCM = new User("root", keyBytes,
                crtBytes, keyBytes, crtBytes);
        userCM.setEnableTxResultDispatcher(true);
        return userCM;

    }

    @Override
    public String getAddrByUserId(Long receiverId) {
        UserPO userInfo = userMapper.selectUserByUserId(receiverId);

        return CryptoUtils.getEVMAddressFromCertBytes(userInfo.getUserCrt().getBytes());

    }


}
