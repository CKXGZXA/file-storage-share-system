package top.ckxgzxa.filestoragesharesystem.cmsdk;

import org.chainmaker.pb.common.Request;
import org.chainmaker.pb.common.ResultOuterClass;
import org.chainmaker.sdk.SdkException;
import org.chainmaker.sdk.User;
import org.chainmaker.sdk.utils.SdkUtils;

import static top.ckxgzxa.filestoragesharesystem.cmsdk.InitClient.chainClient;

/**
 * @author 赵希奥
 * @date 2023/5/30 3:11
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */
public class CertManage {
    public static ResultOuterClass.TxResponse freezeCert(String[] certHashes, User[] endorsers) {
        ResultOuterClass.TxResponse responseInfo = null;
        try {
            Request.Payload payload = chainClient.createCertFreezePayload(certHashes);
            Request.EndorsementEntry[] endorsementEntries = SdkUtils.getEndorsers(payload, endorsers);
            responseInfo = chainClient.freezeCerts(payload, endorsementEntries, InitClient.rpcCallTimeout, InitClient.syncResultTimeout);
        } catch (SdkException e) {
            e.printStackTrace();
        }
        return responseInfo;
    }

    public static ResultOuterClass.TxResponse unfreezeCert(String[] certHashes, User[] endorsers) {
        ResultOuterClass.TxResponse responseInfo = null;
        try {
            Request.Payload payload = chainClient.createPayloadOfUnfreezeCerts(certHashes);
            Request.EndorsementEntry[] endorsementEntries = SdkUtils.getEndorsers(payload, endorsers);
            responseInfo = chainClient.unfreezeCerts(payload, endorsementEntries, InitClient.rpcCallTimeout, InitClient.syncResultTimeout);
        } catch (SdkException e) {
            e.printStackTrace();
        }
        return responseInfo;
    }
}
