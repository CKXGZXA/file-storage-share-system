package top.ckxgzxa.filestoragesharesystem.common.utils;

import cn.hutool.core.convert.NumberWithFormat;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSignerUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import top.ckxgzxa.filestoragesharesystem.domain.po.UserPO;
import top.ckxgzxa.filestoragesharesystem.domain.vo.RoleItem;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author 赵希奥
 * @date 2023/3/16 4:25
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */

public class JwtUtils {

    /**
     * 密钥
     */
    private static final byte[] KEY = "1234567890".getBytes();

    static Log log = LogFactory.get();

    /**
     * 生成jwt
     * @param user
     * @return
     */
    public static String genJwt(UserPO user) {

        ArrayList<RoleItem> roleInfo = user.getIsAdmin() == 1
                ? new ArrayList<RoleItem>() {{
                    add(new RoleItem("管理员", "admin"));
                }}
                : new ArrayList<RoleItem>() {{
                    add(new RoleItem("普通用户", "user"));
                }};

        String token = JWT.create()
                .setPayload("userId", user.getId())
                .setPayload("username", user.getUsername())
                .setPayload("realName", user.getUsername())
                .setPayload("roles", roleInfo)
                .setIssuedAt(DateTime.now())
                .setExpiresAt(DateTime.now().offsetNew(DateField.HOUR,30))
                .setKey(KEY)
                .sign();
        return token;
    }

    /**
     * 验证jwt
     */
    public static boolean validateJwt(String token) {

        boolean isValid = false;

        try {
            JWT jwt = JWT.of(token);
            JWTValidator validator = JWTValidator.of(jwt);
            validator.validateAlgorithm(JWTSignerUtil.hs256(KEY));
            Date now = DateUtil.date();
            validator.validateDate(now, 5);
            isValid = true;
        } catch (ValidateException | IllegalArgumentException e) {
            log.error(e);
        }
        return isValid;
    }

    /**
     * 从JWT中获取用户ID
     */
    public static Long getUserIdFromJwt(String token) {
        JWT jwt = JWTUtil.parseToken(token);

        Long userId;

        try {
           userId = ((NumberWithFormat) jwt.getPayload("userId")).longValue();
        } catch (Exception e) {
            log.error(e);
            userId = (long) -1;
        }

        return userId;
    }

}
