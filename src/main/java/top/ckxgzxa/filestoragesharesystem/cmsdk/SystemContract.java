package top.ckxgzxa.filestoragesharesystem.cmsdk;

import org.chainmaker.pb.common.ChainmakerBlock;
import org.chainmaker.sdk.ChainClient;

/**
 * @author 赵希奥
 * @date 2023/4/7 15:13
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */
public class SystemContract {

    public static void getBlockByHeight(ChainClient chainClient) {
        ChainmakerBlock.BlockInfo blockInfo = null;
        try {
            blockInfo = chainClient.getBlockByHeight(3, false, 10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(blockInfo);
    }
}
