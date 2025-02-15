package top.ckxgzxa.filestoragesharesystem.service.impl;

import cn.hutool.http.HttpStatus;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.chainmaker.pb.common.ResultOuterClass;
import org.chainmaker.sdk.User;
import org.chainmaker.sdk.crypto.ChainMakerCryptoSuiteException;
import org.springframework.stereotype.Service;
import top.ckxgzxa.filestoragesharesystem.cmsdk.Contract;
import top.ckxgzxa.filestoragesharesystem.common.model.Result;
import top.ckxgzxa.filestoragesharesystem.domain.vo.listitem.FileListItem;
import top.ckxgzxa.filestoragesharesystem.domain.vo.FileListResult;
import top.ckxgzxa.filestoragesharesystem.mapper.FileMapper;
import top.ckxgzxa.filestoragesharesystem.mapper.FileShareMapper;
import top.ckxgzxa.filestoragesharesystem.service.CMService;
import top.ckxgzxa.filestoragesharesystem.service.FileManageService;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static top.ckxgzxa.filestoragesharesystem.cmsdk.InitClient.chainClient;

/**
 * @author 赵希奥
 * @date 2023/5/20 1:06
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */

@Service
public class FileManageServiceImpl implements FileManageService {

    @Resource
    private FileMapper fileMapper;

    @Resource
    private FileShareMapper fileShareMapper;

    @Resource
    private CMService cmService;


    @Override
    public Result getFileListPageByUserId(Integer page, Integer pageSize, Long userId) {
        // 开启分页
        PageHelper.startPage(page, pageSize);

        // 获取文件列表
        List<FileListItem> fileList = fileMapper.selectFileListByUserId(userId);

        PageInfo<FileListItem> pageInfo = new PageInfo<>(fileList);

        long total = pageInfo.getTotal();

        FileListResult fileListResult = new FileListResult(fileList, total);

        return Result.success("获取文件列表成功", fileListResult);
    }

    @Override
    public Result shareFile(Long senderId, Long receiverId, Long fileId) {

        // 参数校验, senderId, receiverId不能相同, fileId不能为null
        if (senderId.equals(receiverId)) {
            return Result.failed(HttpStatus.HTTP_FORBIDDEN, "不能给自己分享文件");
        }
        // 校验fileId
        if (fileId == null) {
            return Result.failed(HttpStatus.HTTP_INTERNAL_ERROR, "文件id不能为空");
        }

        // TODO 校验文件是否已经分享给该用户

        String fileUuid = fileMapper.selectUuidByFileId(fileId);

        if (fileUuid == null) {
            return Result.failed(HttpStatus.HTTP_INTERNAL_ERROR, "文件不存在");
        }

        User sender = null;
        try {
            sender = cmService.getCMUserByUserId(senderId);
        } catch (ChainMakerCryptoSuiteException e) {
            return Result.failed(HttpStatus.HTTP_INTERNAL_ERROR, "创建长安链客户端用户失败");
        }
        chainClient.setClientUser(sender);

        // 准备调用合约, 授予接收者访问文件权限
        // 拿到接收者的Hash地址
        String receiverAddr = cmService.getAddrByUserId(receiverId);
        // 生成合约参数, 传入参数fileId(文件的uuid),userAddr
        Map<String, byte[]> params = new HashMap<>();
        params.put("fileId", fileUuid.getBytes());
        params.put("userAddr", receiverAddr.getBytes());

        // 调用合约
        ResultOuterClass.TxResponse responseInfo = Contract.invokeContract(chainClient,
                "FileAuthority", "grantAccess", params);

        String txId = "";

        if (responseInfo != null && responseInfo.getCode() == ResultOuterClass.TxStatusCode.SUCCESS
                && 0 == responseInfo.getContractResult().getCode()) {
            txId = responseInfo.getTxId();
        } else {
            return Result.failed(HttpStatus.HTTP_INTERNAL_ERROR, "用户账户可能被冻结, 请联系管理员");
        }

        // 调用合约成功, 将分享信息插入数据库
        fileShareMapper.insertFileShare(senderId, receiverId, fileId, new Timestamp(System.currentTimeMillis()), txId);

        // 构建返回参数
        Map<String, Object> result = new HashMap<>();
        result.put("txId", txId);

        return Result.success("文件分享成功", result);
    }

    @Override
    public Result delFile(Long userId, Long fileId) {
        // 首先判断文件所有者是否为该用户
        Long ownerId = fileMapper.selectOwnerIdById(fileId);

        if (!ownerId.equals(userId)) {
            return Result.failed(HttpStatus.HTTP_FORBIDDEN, "非法操作, 无权限删除该文件");
        }

        // 删除文件
        fileMapper.deleteFileById(fileId);

        return Result.success("删除文件成功");
    }
}
