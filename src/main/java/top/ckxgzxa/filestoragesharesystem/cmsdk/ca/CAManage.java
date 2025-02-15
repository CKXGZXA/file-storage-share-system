package top.ckxgzxa.filestoragesharesystem.cmsdk.ca;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import lombok.Data;
import top.ckxgzxa.filestoragesharesystem.common.model.Result;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 赵希奥
 * @date 2023/4/12 0:58
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */
public class CAManage {

    private static final Log log = LogFactory.get();

    /**
     * 新建变量: country	string	证书字段（国家）	必填
     */
    private static String country = "CN";

    /**
     * locality	string	证书字段（城市）	必填
     */
    private static String locality = "BeiJing";

    /**
     * province	string	证书字段（省份）	必填
     */
    private static String province = "BeiJing";

    /**
     * ca 服务Host
     */
    private static String genHost = "http://123.57.149.103:8090";

    /**
     * 向 ca 服务器请求新证书
     *
     * @param orgId     组织ID
     * @param userId    用户ID
     * @param userType  用户类型(1.root , 2.ca , 3.admin , 4.client , 5.consensus , 6.common)
     * @param certUsage 证书用途(1.sign , 2.tls , 3.tls-sign , 4.tls-enc)
     */
    public static Result requestNewCert(String orgId, String userId, String userType, String certUsage) {
        // 组装请求参数
        String url = genHost + "/api/ca/gencert";
        Map<String, String> params = new LinkedHashMap<String, String>(7) {
            {
                put("orgId", orgId);
                put("userId", userId);
                put("userType", userType);
                put("certUsage", certUsage);
                put("country", country);
                put("locality", locality);
                put("province", province);
            }
        };
        String bodyString = JSONUtil.formatJsonStr(JSONUtil.toJsonStr(params));
        System.out.println(bodyString);
        String certRes = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .header("Accept", "*/*")
                .keepAlive(true)
                .body(bodyString)
                .execute()
                .body();

        // 将结果转换为Result对象
        CertResult resultObj = JSONUtil.toBean(certRes, CertResult.class);

        switch (resultObj.getCode()) {
            case 200:
                // 将data取出并转换为CertResult对象
                log.info("证书请求成功: \n" + certRes);
                break;
            case 202:
                log.error("请求参数错误: \n" + certRes);
                break;
            case 204:
                log.error("输入参数非法: \n" + certRes);
                break;
            default:
                log.error("证书请求失败: \n" + certRes);
                break;
        }

        Result result = new Result();
        result.setCode(resultObj.getCode());
        result.setMessage(resultObj.getMsg());
        result.setResult(resultObj.getData());

        return result;
    }

}

@Data
class CertResult {
    Integer code;
    String msg;
    Object data;
}