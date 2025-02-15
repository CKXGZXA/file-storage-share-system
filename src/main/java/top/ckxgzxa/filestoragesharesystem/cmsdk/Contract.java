package top.ckxgzxa.filestoragesharesystem.cmsdk;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.chainmaker.pb.common.ContractOuterClass;
import org.chainmaker.pb.common.Request;
import org.chainmaker.pb.common.ResultOuterClass;
import org.chainmaker.sdk.ChainClient;
import org.chainmaker.sdk.User;
import org.chainmaker.sdk.utils.FileUtils;
import org.chainmaker.sdk.utils.SdkUtils;

import java.util.Map;

/**
 * @author 赵希奥
 * @date 2023/4/7 18:51
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */
public class Contract {

    private static final Log log = LogFactory.get();

    // private static final String QUERY_CONTRACT_METHOD = "query";
    // private static final String INVOKE_CONTRACT_METHOD = "increase";
    // private static final String CONTRACT_NAME = "fileContract";
    // private static final String CONTRACT_FILE_PATH = "rust-fact-1.0.0.wasm";

    public static ResultOuterClass.TxResponse createContract(ChainClient chainClient, String contractName, String contractFilePath, User user) {
        ResultOuterClass.TxResponse responseInfo = null;
        try {
            byte[] byteCode = FileUtils.getResourceFileBytes(contractFilePath);

            // 1. create payload
            Request.Payload payload = chainClient.createContractCreatePayload(contractName, "1", byteCode,
                    ContractOuterClass.RuntimeType.WASMER, null);
            // 2. create payloads with endorsement
            Request.EndorsementEntry[] endorsementEntries = SdkUtils
                    .getEndorsers(payload, new User[]{user});

            // 3. send request
            responseInfo = chainClient.sendContractManageRequest(payload, endorsementEntries, 10000, 10000);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        /* System.out.println("创建合约结果：");
        System.out.println(responseInfo); */

        log.info("创建合约结果：\n" + responseInfo);
        return responseInfo;
    }

    public static ResultOuterClass.TxResponse invokeContract(ChainClient chainClient, String contractName, String invokeContractMethod, Map<String, byte[]> params) {
        ResultOuterClass.TxResponse responseInfo = null;
        try {
            responseInfo = chainClient.invokeContract(contractName, invokeContractMethod,
                    null, params, 10000, 10000);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        log.info("执行合约结果：\n" + responseInfo);
        return responseInfo;
    }

    public static ResultOuterClass.TxResponse queryContract(ChainClient chainClient, String contractName, String queryContractMethod, Map<String, byte[]> params) {
        ResultOuterClass.TxResponse responseInfo = null;
        try {
            responseInfo = chainClient.queryContract(contractName, queryContractMethod,
                    null, params, 10000);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        /* System.out.println("查询合约结果：");
        System.out.println(responseInfo); */

        log.info("查询合约结果：\n" + responseInfo);
        return responseInfo;
    }
}
