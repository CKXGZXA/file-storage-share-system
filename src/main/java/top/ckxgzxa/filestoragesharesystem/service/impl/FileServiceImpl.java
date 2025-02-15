package top.ckxgzxa.filestoragesharesystem.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.chainmaker.pb.common.ResultOuterClass;
import org.chainmaker.sdk.User;
import org.chainmaker.sdk.crypto.ChainMakerCryptoSuiteException;
import org.chainmaker.sdk.utils.CryptoUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.ckxgzxa.filestoragesharesystem.cmsdk.Contract;
import top.ckxgzxa.filestoragesharesystem.common.model.Result;
import top.ckxgzxa.filestoragesharesystem.common.utils.IPFSUtils;
import top.ckxgzxa.filestoragesharesystem.common.utils.JwtUtils;
import top.ckxgzxa.filestoragesharesystem.domain.dto.AESResult;
import top.ckxgzxa.filestoragesharesystem.domain.dto.CheckAccessResult;
import top.ckxgzxa.filestoragesharesystem.domain.po.FilePO;
import top.ckxgzxa.filestoragesharesystem.mapper.FileMapper;
import top.ckxgzxa.filestoragesharesystem.mapper.UserMapper;
import top.ckxgzxa.filestoragesharesystem.service.CMService;
import top.ckxgzxa.filestoragesharesystem.service.FileService;

import javax.annotation.Resource;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static top.ckxgzxa.filestoragesharesystem.cmsdk.InitClient.chainClient;

/**
 * @author 赵希奥
 * @date 2023/4/7 10:23
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */

@Service
public class FileServiceImpl implements FileService {

    private final Log log = LogFactory.get();

    @Resource
    private FileMapper fileMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CMService cmService;

    @Override
    @Deprecated
    public Result saveFile(FilePO file) {
        // 对文件进行校验
        if (file == null) {
            return Result.failed(HttpStatus.HTTP_INTERNAL_ERROR, "文件为空");
        }
        if (file.getCid() == null || file.getCid().equals("")) {
            return Result.failed(HttpStatus.HTTP_INTERNAL_ERROR, "文件cid为空");
        }
        // 校验IPFS的CID是否合法
        if (file.getCid().length() != 46) {
            return Result.failed(HttpStatus.HTTP_INTERNAL_ERROR, "文件cid不合法");
        }
        // 校验文件是否已存在
        FilePO file1 = fileMapper.findSFileByCID(file.getCid());
        if (file1 != null) {
            return Result.failed(HttpStatus.HTTP_INTERNAL_ERROR, "文件已存在");
        }
        fileMapper.insertSFile(file);
        Map<String, byte[]> params = new HashMap<>();
        params.put("file_hash", file.getCid().getBytes());
        params.put("file_name", file.getFileName().getBytes());
        Contract.invokeContract(chainClient, "fileContract", "save", params);

        return Result.success("保存成功");
    }

    @Override
    @Deprecated
    public Result getUrlFromCid(String cid) {
        // 校验IPFS的CID是否合法
        if (cid.length() != 46) {
            return Result.failed(HttpStatus.HTTP_INTERNAL_ERROR, "文件cid不合法");
        }
        // 校验文件是否已存在
        FilePO file1 = fileMapper.findSFileByCID(cid);
        if (file1 == null) {
            return Result.failed(HttpStatus.HTTP_INTERNAL_ERROR, "文件不存在");
        }
        return Result.success("获取成功", "https://ipfs.moralis.io:2053/ipfs/" + file1.getCid() + "/" + file1.getFileName());
    }

    @Override
    public Integer addFile(FilePO filePO) {
        return fileMapper.insertSFile(filePO);
    }

    @Override
    public Boolean selectFileByMd5(String md5) {
        FilePO filePO = fileMapper.selectFileByMd5(md5);
        return filePO != null;
    }


    @Override
    public List<FilePO> selectFileList() {
        List<FilePO> list = fileMapper.selectFileList();
        return list;
    }

    @Override
    public Result upload(MultipartFile file, String token) {
        // 从token中获取用户id
        Long userId = JwtUtils.getUserIdFromJwt(token);

        // 按照userId新建长安链用户
        User userCM;
        try {
            userCM = cmService.getCMUserByUserId(userId);
        } catch (ChainMakerCryptoSuiteException e) {
            return Result.failed(HttpStatus.HTTP_INTERNAL_ERROR, "长安链客户端用户创建失败");
        }
        // 切换当前客户端用户
        chainClient.setClientUser(userCM);
        // 获取文件名
        String fileName = file.getOriginalFilename();

        // 获取文件大小
        long fileSize = file.getSize();

        // 读取文件字节
        byte[] fileBytes;
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            return Result.failed(HttpStatus.HTTP_INTERNAL_ERROR, "文件读取失败");
        }

        // 获取文件md5
        Digester md5Digester = new Digester(DigestAlgorithm.MD5);

        String md5 = md5Digester.digestHex(fileBytes);

        // todo 暂时查MySQL, 可改为调查询合约判断文件是否已经存在
        Boolean isUploaded = this.selectFileByMd5(md5);

        byte[] aesKey;

        // 如果已存在, 则从区块链上读取文件加密密钥
        if (isUploaded) {
            // 从区块链上读取文件加密密钥
            Map<String, byte[]> params = new HashMap<>();
            params.put("fileMd5", md5.getBytes());

            ResultOuterClass.TxResponse responseInfo = Contract.queryContract(chainClient,
                    "EncryptionManage", "getKey", params);
            if (!(responseInfo != null && responseInfo.getCode() == ResultOuterClass.TxStatusCode.SUCCESS && 0 == responseInfo.getContractResult().getCode()) ) {
                return Result.failed(HttpStatus.HTTP_UNAUTHORIZED, "文件上传失败(Get Key Failed)");
            }

            String aesKeyStr = responseInfo.getContractResult().getResult().toStringUtf8();

            AESResult aesResult = JSONUtil.toBean(aesKeyStr, AESResult.class);

            aesKey = aesResult.getAesKey();

        } else {
            // 构建新AES密钥
            aesKey = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
            log.info("文件(" + md5 + ")的密钥生成为: " + Arrays.toString(aesKey));
        }

        // 以AES密钥构建加密器
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, aesKey);
        // 加密文件
        byte[] encryptFileBytes = aes.encrypt(fileBytes);
        // 将加密后的文件上传到IPFS, 并获取文件的CID
        String cid = "";
        try {
            cid = IPFSUtils.upload(encryptFileBytes);
        } catch (IOException e) {
            return Result.failed(HttpStatus.HTTP_INTERNAL_ERROR, "文件上传至IPFS失败(02)");
        }

        // 生成文件UUID
        String fileUuid = UUID.fastUUID().toString();

        // 获取当前时间
        long fileTime = System.currentTimeMillis();

        // 将文件信息上链
        // 包含参数fileId,fileName,fileSize,fileHash,fileMd5,userAddr,fileTime,aesKey
        Map<String, byte[]> params = new HashMap<>();
        params.put("fileId", fileUuid.getBytes());
        params.put("fileName", fileName.getBytes());
        params.put("fileSize", Long.toString(fileSize).getBytes());
        params.put("fileHash", cid.getBytes());
        params.put("fileMd5", md5.getBytes());
        params.put("userAddr", CryptoUtils.getEVMAddressFromCertBytes(userCM.getCertBytes()).getBytes());
        params.put("fileTime", Long.toString(fileTime).getBytes());
        params.put("aesKey", aesKey);

        ResultOuterClass.TxResponse responseInfo = Contract.invokeContract(chainClient,
                "FileManage", "addFile", params);

        String txId = "";

        if (responseInfo != null && responseInfo.getCode() == ResultOuterClass.TxStatusCode.SUCCESS && 0 == responseInfo.getContractResult().getCode()) {
            txId = responseInfo.getTxId();
        } else {
            return Result.failed(HttpStatus.HTTP_INTERNAL_ERROR, "文件上传失败(Upload Chain Failed)");
        }

        // 将文件信息存入MySQL数据库
        FilePO filePO = new FilePO();
        filePO.setFileName(fileName);
        filePO.setUuid(fileUuid);
        filePO.setCid(cid);
        filePO.setSize(fileSize);
        filePO.setMd5(md5);
        // 将时间戳转换为TimeStamp
        Timestamp timestamp = new Timestamp(fileTime);
        filePO.setUploadTime(timestamp);
        filePO.setOwnerId(userId);
        filePO.setTxId(txId);

        fileMapper.insertSFile(filePO);

        Map<String, Object> map = new HashMap<>();
        map.put("txId", txId);

        return Result.success("文件上传成功", map);

    }

    @Override
    public boolean checkFileAccess(Long userId, Long fileId) throws ChainMakerCryptoSuiteException {
        // 根据用户Id生成长安链客户端用户
        User userCM = cmService.getCMUserByUserId(userId);

        // 切换当前客户端用户
        chainClient.setClientUser(userCM);

        // 通过文件id拿到文件uuid
        String fileUuid = fileMapper.selectUuidByFileId(fileId);

        // 构建查询参数
        Map<String, byte[]> params = new HashMap<>(2);
        params.put("fileId", fileUuid.getBytes());
        params.put("userAddr", CryptoUtils.getEVMAddressFromCertBytes(userCM.getCertBytes()).getBytes());

        ResultOuterClass.TxResponse responseInfo = Contract.queryContract(chainClient,
                "FileAuthority", "checkAccess", params);

        if (responseInfo != null && responseInfo.getCode() == ResultOuterClass.TxStatusCode.SUCCESS && 0 == responseInfo.getContractResult().getCode()) {
            CheckAccessResult checkAccessResult = JSONUtil.toBean(
                    responseInfo.getContractResult().getResult().toStringUtf8(), CheckAccessResult.class);
            if (0 != checkAccessResult.getAccess()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public byte[] downloadFromIPFS(Long userId, Long fileId) throws Exception {
        // 首先拿到文件信息
        FilePO fileInfo = fileMapper.selectFileById(fileId);

        // 根据用户Id生成长安链客户端用户
        User userCM = null;
        try {
            userCM = cmService.getCMUserByUserId(userId);
        } catch (ChainMakerCryptoSuiteException e) {
            throw new RuntimeException(e);
        }

        // 切换当前客户端用户
        chainClient.setClientUser(userCM);


        // 从区块链上读取文件加密密钥
        Map<String, byte[]> params = new HashMap<>(1);
        params.put("fileMd5", fileInfo.getMd5().getBytes());

        ResultOuterClass.TxResponse responseInfo = Contract.queryContract(chainClient,
                "EncryptionManage", "getKey", params);
        if (responseInfo == null && 0 != responseInfo.getContractResult().getCode()) {
            throw new Exception("获取加密密钥失败!");
        }

        String aesKeyStr = responseInfo.getContractResult().getResult().toStringUtf8();

        AESResult aesResult = JSONUtil.toBean(aesKeyStr, AESResult.class);

        byte[] aesKey = aesResult.getAesKey();

        log.info("文件(" + aesResult.getMd5() + ")的密钥从链上读出为: " + Arrays.toString(aesKey));

        // 从IPFS下载加密文件
        byte[] decryptedFile = IPFSUtils.download(fileInfo.getCid());

        // 以AES密钥构建加密器
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, aesKey);

        // 解密文件
        byte[] file = aes.decrypt(decryptedFile);

        return file;

    }
}
