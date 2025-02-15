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
import top.ckxgzxa.filestoragesharesystem.domain.vo.listitem.ShareFromListItem;
import top.ckxgzxa.filestoragesharesystem.domain.vo.ShareFromListResult;
import top.ckxgzxa.filestoragesharesystem.domain.vo.listitem.ShareToListItem;
import top.ckxgzxa.filestoragesharesystem.domain.vo.ShareToListResult;
import top.ckxgzxa.filestoragesharesystem.mapper.FileMapper;
import top.ckxgzxa.filestoragesharesystem.mapper.FileShareMapper;
import top.ckxgzxa.filestoragesharesystem.mapper.ShareListMapper;
import top.ckxgzxa.filestoragesharesystem.service.CMService;
import top.ckxgzxa.filestoragesharesystem.service.ShareManageService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static top.ckxgzxa.filestoragesharesystem.cmsdk.InitClient.chainClient;

/**
 * @author 赵希奥
 * @date 2023/5/24 3:53
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */

@Service
public class ShareManageServiceImpl implements ShareManageService {

    @Resource
    private ShareListMapper shareListMapper;

    @Resource
    private FileShareMapper fileShareMapper;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private CMService cmService;

    @Override
    public Result getFromListPageByUserId(Integer page, Integer pageSize, Long userId) {
        // 开启分页
        PageHelper.startPage(page, pageSize);

        // 获取我分享的文件列表
        List<ShareFromListItem> shareFromList = shareListMapper.selectFromListByUserId(userId);

        PageInfo<ShareFromListItem> pageInfo = new PageInfo<>(shareFromList);

        long total = pageInfo.getTotal();

        ShareFromListResult shareFromListResult = new ShareFromListResult(shareFromList, total);

        return Result.success("获取我分享的文件列表成功", shareFromListResult);
    }

    @Override
    public Result getToListPageByUserId(Integer page, Integer pageSize, Long userId) {
        // 开启分页
        PageHelper.startPage(page, pageSize);

        // 获取分享给我的文件列表
        List<ShareToListItem> shareToList = shareListMapper.selectToListByUserId(userId);

        PageInfo<ShareToListItem> pageInfo = new PageInfo<>(shareToList);

        long total = pageInfo.getTotal();

        ShareToListResult shareToListResult = new ShareToListResult(shareToList, total);

        return Result.success("获取分享给我的文件列表成功", shareToListResult);
    }

    @Override
    public Result revokeShare(Long cmUserId, Long fileId, Long userId) {
        // 判断是否是文件所有者
        // 1. 查出该文件所有者的id
        Long userOwnerId = fileMapper.selectOwnerIdById(fileId);
        // 2. 判断该文件所有者的id是否等于当前用户id
        if (!userOwnerId.equals(cmUserId)) {
            // 如果不是，返回错误信息
            return Result.failed(HttpStatus.HTTP_FORBIDDEN, "您不是该文件的所有者，无法撤销该文件的访问权限");
        }
        // 以当前用户id新建长安链客户端用户
        User cmUser;
        try {
            cmUser = cmService.getCMUserByUserId(cmUserId);
        } catch (ChainMakerCryptoSuiteException e) {
            return Result.failed(HttpStatus.HTTP_INTERNAL_ERROR, "撤销失败(chainmaker create failed 01)");
        }
        // 切换到长安链用户
        chainClient.setClientUser(cmUser);

        // 调用长安链接口，撤销该用户对该文件的访问权限
        // 1. 构建参数
        String uuid = fileMapper.selectUuidByFileId(fileId);
        String userAddr = cmService.getAddrByUserId(userId);
        Map<String, byte[]> params = new HashMap<>(2);
        params.put("fileId", uuid.getBytes());
        params.put("userAddr", userAddr.getBytes());
        // 2. 调用长安链接口
        ResultOuterClass.TxResponse responseInfo = Contract.invokeContract(chainClient, "FileAuthority",
                "revokeAccess", params);

        String txId;

        if (null != responseInfo && 0 == responseInfo.getContractResult().getCode()) {
            // 如果调用成功，获取交易id
            txId = responseInfo.getTxId();
        } else {
            // 如果调用失败，返回错误信息
            return Result.failed(HttpStatus.HTTP_INTERNAL_ERROR, "撤销失败(chainmaker contract failed)");
        }

        // 调用成功，删除该条分享记录
        fileShareMapper.deleteFileShare(cmUserId, userId, fileId);

        Map<String, Object> result = new HashMap<>(1);
        result.put("txId", txId);
        return Result.success("文件分享撤销成功", result);
    }
}
