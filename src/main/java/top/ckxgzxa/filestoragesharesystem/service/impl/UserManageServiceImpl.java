package top.ckxgzxa.filestoragesharesystem.service.impl;

import cn.hutool.http.HttpStatus;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.chainmaker.pb.common.ResultOuterClass;
import org.chainmaker.sdk.User;
import org.chainmaker.sdk.crypto.ChainMakerCryptoSuiteException;
import org.springframework.stereotype.Service;
import top.ckxgzxa.filestoragesharesystem.cmsdk.CertManage;
import top.ckxgzxa.filestoragesharesystem.common.model.Result;
import top.ckxgzxa.filestoragesharesystem.domain.po.UserPO;
import top.ckxgzxa.filestoragesharesystem.domain.vo.UserListResult;
import top.ckxgzxa.filestoragesharesystem.domain.vo.listitem.UserListItem;
import top.ckxgzxa.filestoragesharesystem.mapper.UserMapper;
import top.ckxgzxa.filestoragesharesystem.service.UserManageService;

import javax.annotation.Resource;
import java.util.List;

import static top.ckxgzxa.filestoragesharesystem.cmsdk.InitClient.chainClient;

/**
 * @author 赵希奥
 * @date 2023/5/30 20:54
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */
@Service
public class UserManageServiceImpl implements UserManageService {

    private final Log log = LogFactory.get();

    @Resource
    private UserMapper userMapper;


    @Override
    public Result freezeUser(Long userId, Long adminId) {

        UserPO admin = userMapper.selectUserByUserId(adminId);

        // 获取待处理用户对象
        UserPO user = userMapper.selectUserByUserId(userId);
        // 判断adminId是否具有管理员权限
        if (admin.getIsAdmin() != 1 ) {
            return Result.failed(HttpStatus.HTTP_FORBIDDEN, "用户权限不足");
        } else if (user.getIsAdmin() != 0) {
            return Result.failed(HttpStatus.HTTP_FORBIDDEN, "冻结账户为管理员账户");
        }

        // TODO 多组织, 需进行多签, 需在此处进行改动, 目前单组织仅需传一个共识节点
        User endorser;
        try {
            endorser = new User("root", admin.getUserSignKey().getBytes(), admin.getUserSignCrt().getBytes(),
                    admin.getUserKey().getBytes(), admin.getUserCrt().getBytes());
            endorser.setEnableTxResultDispatcher(true);
        } catch (ChainMakerCryptoSuiteException e) {
            return Result.failed(HttpStatus.HTTP_INTERNAL_ERROR, "服务器内部错误!");
        }


        chainClient.setClientUser(endorser);
        ResultOuterClass.TxResponse responseInfo = CertManage.freezeCert(new String[]{user.getUserCrt()}, new User[]{endorser});

        log.info("responseInfo: " + responseInfo);

        if (responseInfo.getCode() != ResultOuterClass.TxStatusCode.SUCCESS) {
            return Result.failed(HttpStatus.HTTP_INTERNAL_ERROR, "服务器内部错误!");
        }

        // 调用mapper接口冻结用户
        userMapper.freezeUser(userId);

        return Result.success("冻结用户成功");
    }

    @Override
    public Result unfreezeUser(Long userId, Long adminId) {

        UserPO admin = userMapper.selectUserByUserId(adminId);

        // 获取待处理用户对象
        UserPO user = userMapper.selectUserByUserId(userId);
        // 判断adminId是否具有管理员权限
        if (admin.getIsAdmin() != 1 || user.getIsAdmin() != 0) {
            return Result.failed(HttpStatus.HTTP_FORBIDDEN, "用户权限不足");
        }

        // TODO 多组织, 需进行多签, 需在此处进行改动, 目前单组织仅需传一个共识
        User endorser;
        try {
            endorser = new User("root", admin.getUserSignKey().getBytes(), admin.getUserSignCrt().getBytes(),
                    admin.getUserKey().getBytes(), admin.getUserCrt().getBytes());
            endorser.setEnableTxResultDispatcher(true);
        } catch (ChainMakerCryptoSuiteException e) {
            return Result.failed(HttpStatus.HTTP_INTERNAL_ERROR, "服务器内部错误!");
        }

        chainClient.setClientUser(endorser);
        ResultOuterClass.TxResponse responseInfo = CertManage.unfreezeCert(new String[]{user.getUserCrt()}, new User[]{endorser});

        log.info("responseInfo: " + responseInfo);

        if (responseInfo.getCode() != ResultOuterClass.TxStatusCode.SUCCESS) {
            return Result.failed(HttpStatus.HTTP_INTERNAL_ERROR, "服务器内部错误!");
        }

        // 调用mapper接口解冻用户
        userMapper.unfreezeUser(userId);

        return Result.success("解冻用户成功");
    }

    @Override
    public Result getUserListPage(Integer page, Integer pageSize) {
        // 开启分页
        PageHelper.startPage(page, pageSize);

        // 获取用户列表
        List<UserListItem> userList = userMapper.selectUserList();

        PageInfo<UserListItem> pageInfo = new PageInfo<>(userList);

        long total = pageInfo.getTotal();

        UserListResult userListResult = new UserListResult(userList, total);

        return Result.success("获取用户列表成功", userListResult);
    }

}
