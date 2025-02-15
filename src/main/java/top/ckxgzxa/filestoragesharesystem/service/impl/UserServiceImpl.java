package top.ckxgzxa.filestoragesharesystem.service.impl;

import cn.hutool.core.convert.NumberWithFormat;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import org.chainmaker.sdk.utils.CryptoUtils;
import org.springframework.stereotype.Service;
import top.ckxgzxa.filestoragesharesystem.cmsdk.ca.CAManage;
import top.ckxgzxa.filestoragesharesystem.cmsdk.ca.CertData;
import top.ckxgzxa.filestoragesharesystem.common.model.Result;
import top.ckxgzxa.filestoragesharesystem.common.utils.JwtUtils;
import top.ckxgzxa.filestoragesharesystem.domain.dto.ResetPassParams;
import top.ckxgzxa.filestoragesharesystem.domain.dto.UserLoginDTO;
import top.ckxgzxa.filestoragesharesystem.domain.po.UserPO;
import top.ckxgzxa.filestoragesharesystem.domain.vo.RoleItem;
import top.ckxgzxa.filestoragesharesystem.domain.vo.UserInfo;
import top.ckxgzxa.filestoragesharesystem.domain.vo.UserOption;
import top.ckxgzxa.filestoragesharesystem.mapper.UserMapper;
import top.ckxgzxa.filestoragesharesystem.service.UserService;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 赵希奥
 * @date 2023/3/17 2:51
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public Result register(UserPO user) {
        if (user == null || user.getUsername() == null || user.getPassword() == null /*|| user.getOrgId() == null*/) {
            // 如果用户对象或者其属性为空，则返回false表示注册失败
            return Result.failed(HttpStatus.HTTP_BAD_REQUEST, "参数错误");
        }

        // // 新建Organization, 并以其id为参数查询数据库
        // Organization organization = orgMapper.selectOrgById(user.getOrgId());
        //
        // //判断orgId是否合法
        // if (null == organization) {
        //     return Result.failed(HttpStatus.HTTP_BAD_REQUEST, "机构id不存在");
        // }


        // 获取用户名
        String username = user.getUsername();
        // 调用mapper接口根据用户名查询用户
        UserPO existingUser = userMapper.findUserByUsername(username);
        if (existingUser != null) {
            // 如果已经存在同名的用户，则返回false表示注册失败
            return Result.failed(HttpStatus.HTTP_CONFLICT, "用户名已存在");
        }
        // 获取明文密码`
        String rawPassword = user.getPassword();
        // 生成盐
        String salt = BCrypt.gensalt();
        // 存储盐
        user.setSalt(salt);
        // 对明文密码进行编码，生成加密后的密码
        String encodedPassword = BCrypt.hashpw(rawPassword, salt);
        // 将加密后的密码设置到用户对象中
        user.setPassword(encodedPassword);

        /* String.valueOf(user.getOrgId()) */
        Result resp = CAManage.requestNewCert( "root",
                user.getUsername(), "client", "tls-sign");

        // 组装用户的公钥和私钥到一个Map作为注册成功的返回数据
        Map<String, String> data = new HashMap<>(2);

        // 验证resp的合法性
        if ( HttpStatus.HTTP_OK != resp.getCode()) {
            resp.setMessage("ca证书服务: " + resp.getMessage());
            return resp;
        } else {
            // 将证书赋值给user
            CertData certData = JSONUtil.toBean(resp.getResult().toString(), CertData.class) ;
            user.setUserCrt(certData.getCert());
            user.setUserKey(certData.getPrivateKey());
            user.setUserSignCrt(certData.getCert());
            user.setUserSignKey(certData.getPrivateKey());
            data.put("cert", certData.getCert());
            data.put("key", certData.getPrivateKey());

            // 保存user的账户Hash地址
            user.setAddr(CryptoUtils.getEVMAddressFromCertBytes(certData.getCert().getBytes()));

        }

        user.setIsAdmin(0);
        user.setCreateTime(new Timestamp(System.currentTimeMillis()));

        // 调用mapper接口插入用户对象，并获取影响的行数
        int result = userMapper.insertUser(user);

        // 如果影响的行数大于0，则返回true表示注册成功，否则返回false表示注册失败
        return result > 0
                ? Result.success("注册成功, 系统自动下载公私钥, 请妥善保存, 私钥是找回密码的唯一凭证", data)
                : Result.failed(HttpStatus.HTTP_INTERNAL_ERROR, "服务器内部错误");
    }

    @Override
    public Result login(UserLoginDTO userLoginDTO) {
        // 根据用户名获取用户对象
        UserPO user = userMapper.findUserByUsername(userLoginDTO.getUsername());
        // 如果用户对象为空，则返回null表示登录失败
        if (user == null) {
            return Result.failed(HttpStatus.HTTP_NOT_FOUND, "用户不存在");
        }
        // 使用user中的存储的盐对用户输入的明文密码进行加密
        String encodedPassword = BCrypt.hashpw(userLoginDTO.getPassword(), user.getSalt());
        // 如果加密后的密码与user中的密码相同
        if (encodedPassword.equals(user.getPassword())) {
            // 生成token
            String token = JwtUtils.genJwt(user);

            // 更新数据库token字段
            userMapper.updateTokenById(user.getId(), token);

            // 组装返回数据
            Map<String, Object> data = new HashMap<>(3);
/*             // 将user封装到UserVO中
            UserVO userVO = new UserVO();
            userVO.setId(user.getId());
            userVO.setUsername(user.getUsername());
            // TODO 机构id, 待实现
            // userVO.setOrgId(user.getOrgId());
            userVO.setAddr(user.getAddr());

            data.put("user", userVO);
            data.put("token", token);
            data.put("isAdmin", user.getIsAdmin() == 1); */

            data.put("userId", user.getId());
            data.put("token", token);
            if (user.getIsAdmin() == 1) {
                RoleItem roleItem = new RoleItem();
                roleItem.setRoleName("管理员");
                roleItem.setValue("admin");
                data.put("role", new ArrayList<RoleItem>(1){{add(roleItem);}});
            } else {
                RoleItem roleItem = new RoleItem();
                roleItem.setRoleName("普通用户");
                roleItem.setValue("user");
                data.put("role", new ArrayList<RoleItem>(1){{add(roleItem);}});
            }

            // 返回登录成功
            return Result.success("登录成功", data);
        } else {
            // 如果加密后的密码与user中的密码不同，则返回null表示登录失败
            return Result.failed(HttpStatus.HTTP_UNAUTHORIZED, "用户名或密码错误");
        }
    }

    @Override
    public Result logout(String token) {
        // 解析token获取用户id
        JWT jwt = JWTUtil.parseToken(token);
        Long userId = ((NumberWithFormat) jwt.getPayload("userId")).longValue();
        // 调用mapper接口删除用户的token字段
        int result = userMapper.deleteTokenById(userId);
        // 如果影响的行数大于0，则返回true表示登出成功，否则返回false表示登出失败
        return result > 0
                ? Result.success("登出成功")
                : Result.failed(HttpStatus.HTTP_INTERNAL_ERROR, "服务器内部错误");
    }

    @Override
    public Result getUserOptions(String key) {
        List<UserOption> userOptions = userMapper.selectUserOptions(key);

        return Result.success("ok", userOptions);
    }

    @Override
    public Result getAccountInfo(Long userId) {
        UserInfo userInfo = userMapper.selectAccountInfo(userId);
        return Result.success("获取个人信息成功", userInfo);
    }

    @Override
    public Result resetPassword(ResetPassParams resetParams) {
        // 判定用户名是否存在
        UserPO user = userMapper.findUserByUsername(resetParams.getUsername());
        if (user == null) {
            return Result.failed(HttpStatus.HTTP_NOT_FOUND, "用户不存在, 请检查用户名");
        }

        // 判定私钥是否正确
        String addr = CryptoUtils.getEVMAddressFromPrivateKeyBytes(resetParams.getPrivateKey().getBytes(), "sha256");
        if (!addr.equals(user.getAddr())) {
            return Result.failed(HttpStatus.HTTP_BAD_REQUEST, "私钥不正确, 请检查私钥");
        }

        // 生成盐
        String salt = BCrypt.gensalt();
        // 存储盐
        user.setSalt(salt);
        // 对明文密码进行编码，生成加密后的密码
        String encodedPassword = BCrypt.hashpw(resetParams.getNewPassword(), salt);
        // 将加密后的密码设置到用户对象中
        user.setPassword(encodedPassword);

        // 调用mapper接口插入用户对象，并获取影响的行数
        int result = userMapper.updatePassword(user.getId(), user.getPassword(), user.getSalt());

        // 如果影响的行数大于0，则返回true表示注册成功，否则返回false表示注册失败
        return result > 0
                ? Result.success("密码重置成功")
                : Result.failed(HttpStatus.HTTP_INTERNAL_ERROR, "服务器内部错误");
    }
}
