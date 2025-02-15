package top.ckxgzxa.filestoragesharesystem.cmsdk;

/**
 * @author 赵希奥
 * @date 2023/4/1 23:58
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */

import org.chainmaker.sdk.*;
import org.chainmaker.sdk.config.*;
import org.chainmaker.sdk.utils.FileUtils;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class InitClient {

    private static String nodeGrpcUrl = "192.168.56.105:12301";

    static int connCnt = 10;

    static int maxMessageSize = 16;

    static long rpcCallTimeout = 10000;

    static long syncResultTimeout = 10000;

    static String SDK_CONFIG = "sdk_config.yml";

    public static ChainClient chainClient;
    static ChainManager chainManager;
    static User user;

    public static User adminUser;

    public static User user1;

    public static ChainClient initChainClient(String chainId, String orgId, String orgCert, String userKey, String userCrt, String userSignKey, String userSignCrt) throws SdkException {

        // ca
        byte[][] tlsCaCerts = new byte[][]{orgCert.getBytes()};

        // 初始化sdk配置
        SdkConfig sdkConfig = new SdkConfig();
        ChainClientConfig chainClientConfig = new ChainClientConfig();
        sdkConfig.setChainClient(chainClientConfig);

        RpcClientConfig rpcClientConfig = new RpcClientConfig();
        rpcClientConfig.setMaxReceiveMessageSize(maxMessageSize);

        // 新建NodeConfig
        NodeConfig nodeConfig = new NodeConfig();
        nodeConfig.setTrustRootBytes(tlsCaCerts);
        nodeConfig.setEnableTls(false);
        nodeConfig.setNodeAddr(nodeGrpcUrl);
        nodeConfig.setConnCnt(connCnt);
        NodeConfig[] nodeConfigs = new NodeConfig[]{nodeConfig};

        chainManager = ChainManager.getInstance();
        chainClient = chainManager.getChainClient(chainId);
        chainClientConfig.setOrgId(orgId);
        chainClientConfig.setChainId(chainId);

        // 连接链的当前用户
        chainClientConfig.setUserKeyBytes(userKey.getBytes());
        chainClientConfig.setUserCrtBytes(userCrt.getBytes());
        chainClientConfig.setUserSignKeyBytes(userSignKey.getBytes());
        chainClientConfig.setUserSignCrtBytes(userSignCrt.getBytes());
        chainClientConfig.setRpcClient(rpcClientConfig);
        chainClientConfig.setNodes(nodeConfigs);


        return chainManager.createChainClient(sdkConfig);

    }

    /**
     * 不通过配置文件初始化
     *
     * @throws SdkException
     */
    public static void initWithNoConfig() throws SdkException {
        // byte[][] tlsCaCerts = new byte[][]{FileUtils.getResourceFileBytes(ORG1_CERT_PATH)};
        final String ORG1_CERT = "-----BEGIN CERTIFICATE-----\n" +
                "MIICYTCCAgigAwIBAgIDBB/7MAoGCCqGSM49BAMCMHIxCzAJBgNVBAYTAmNuMRAw\n" +
                "DgYDVQQIEwdiZWlqaW5nMRAwDgYDVQQHEwdiZWlqaW5nMRMwEQYDVQQKEwpUZXN0\n" +
                "Q01vcmcxMRIwEAYDVQQLEwlyb290LWNlcnQxFjAUBgNVBAMTDWNhLmNtdGVzdG9y\n" +
                "ZzEwHhcNMjMwNDAyMjE0MTU3WhcNMzIwMzMwMjE0MTU3WjByMQswCQYDVQQGEwJj\n" +
                "bjEQMA4GA1UECBMHYmVpamluZzEQMA4GA1UEBxMHYmVpamluZzETMBEGA1UEChMK\n" +
                "VGVzdENNb3JnMTESMBAGA1UECxMJcm9vdC1jZXJ0MRYwFAYDVQQDEw1jYS5jbXRl\n" +
                "c3RvcmcxMFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAELbhI0xSchIiUC0YwSaFH\n" +
                "MbJVTU0iRb7gBACvwa7ASzIzP/yoc9w1EnXaL6h2DXgyFP/crdjgYo1LNruVLzMV\n" +
                "QqOBjDCBiTAOBgNVHQ8BAf8EBAMCAaYwDwYDVR0lBAgwBgYEVR0lADAPBgNVHRMB\n" +
                "Af8EBTADAQH/MCkGA1UdDgQiBCBZHwehO00cXlqIaAWsf3CnBp40P7PpmDOw5kI+\n" +
                "xmOH+jAqBgNVHREEIzAhgglsb2NhbGhvc3SCDmNoYWlubWFrZXIub3JnhwR/AAAB\n" +
                "MAoGCCqGSM49BAMCA0cAMEQCIGKgqc2i2MzHOhcz0XZsqqrLULDgWV/JweQHE3Kw\n" +
                "MD4uAiBExu2y7GH7QJx3jXLqbGJPQ1vDggYn+tqJAPfuCCQOEg==\n" +
                "-----END CERTIFICATE-----\n";
        final String NODE_GRPC_URL1 = "192.168.56.105:12301";
        final int CONNECT_COUNT = 10;
        final String CHAIN_ID = "chain1";


        byte[][] tlsCaCerts = new byte[][]{ORG1_CERT.getBytes()};

        SdkConfig sdkConfig = new SdkConfig();
        ChainClientConfig chainClientConfig = new ChainClientConfig();
        sdkConfig.setChainClient(chainClientConfig);

        RpcClientConfig rpcClientConfig = new RpcClientConfig();
        rpcClientConfig.setMaxReceiveMessageSize(16);

        NodeConfig nodeConfig = new NodeConfig();
        nodeConfig.setTrustRootBytes(tlsCaCerts);
        nodeConfig.setEnableTls(false);
        nodeConfig.setNodeAddr(NODE_GRPC_URL1);
        nodeConfig.setConnCnt(CONNECT_COUNT);
        NodeConfig[] nodeConfigs = new NodeConfig[]{nodeConfig};
        chainManager = ChainManager.getInstance();
        chainClient = chainManager.getChainClient(CHAIN_ID);

        chainClientConfig.setOrgId("TestOrg1");
        chainClientConfig.setChainId(CHAIN_ID);
        // chainClientConfig.setUserKeyBytes(FileUtils.getResourceFileBytes(CLIENT1_TLS_KEY_PATH));
        chainClientConfig.setUserKeyBytes(("-----BEGIN EC PRIVATE KEY-----\n" +
                "MHcCAQEEIFQ0+QDggjjJzVOt0orOJhBpTgntOij0EgudFxBbbzthoAoGCCqGSM49\n" +
                "AwEHoUQDQgAEy6bT8zpL+jbf+rrAkw0FyeEKVygd8sYfjkyeC5bntYYDRP3H+p1M\n" +
                "5F7h4FbqdeLZHSSsct03Hh98W9VoYgvniw==\n" +
                "-----END EC PRIVATE KEY-----").getBytes());
        // chainClientConfig.setUserCrtBytes(FileUtils.getResourceFileBytes(CLIENT1_TLS_CERT_PATH));
        chainClientConfig.setUserCrtBytes(("-----BEGIN CERTIFICATE-----\n" +
                "MIICiDCCAi2gAwIBAgIDCISBMAoGCCqGSM49BAMCMHIxCzAJBgNVBAYTAmNuMRAw\n" +
                "DgYDVQQIEwdiZWlqaW5nMRAwDgYDVQQHEwdiZWlqaW5nMRMwEQYDVQQKEwpUZXN0\n" +
                "Q01vcmcxMRIwEAYDVQQLEwlyb290LWNlcnQxFjAUBgNVBAMTDWNhLmNtdGVzdG9y\n" +
                "ZzEwHhcNMjMwNDAyMjE0MTU3WhcNMzIwMzMwMjE0MTU3WjB7MQswCQYDVQQGEwJj\n" +
                "bjEQMA4GA1UECBMHYmVpamluZzEQMA4GA1UEBxMHYmVpamluZzETMBEGA1UEChMK\n" +
                "VGVzdENNb3JnMTEOMAwGA1UECxMFYWRtaW4xIzAhBgNVBAMTGmNtdGVzdHVzZXIx\n" +
                "LnRscy5UZXN0Q01vcmcxMFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEy6bT8zpL\n" +
                "+jbf+rrAkw0FyeEKVygd8sYfjkyeC5bntYYDRP3H+p1M5F7h4FbqdeLZHSSsct03\n" +
                "Hh98W9VoYgvni6OBqDCBpTAOBgNVHQ8BAf8EBAMCAaYwDwYDVR0lBAgwBgYEVR0l\n" +
                "ADApBgNVHQ4EIgQgmMXEawHGeBVGUS8z66g9OW3R8vM59yGsiq9Aufe+fRswKwYD\n" +
                "VR0jBCQwIoAgWR8HoTtNHF5aiGgFrH9wpwaeND+z6ZgzsOZCPsZjh/owKgYDVR0R\n" +
                "BCMwIYIJbG9jYWxob3N0gg5jaGFpbm1ha2VyLm9yZ4cEfwAAATAKBggqhkjOPQQD\n" +
                "AgNJADBGAiEA6tIvLY3Ex70wn344KUjavL7AFKLXJuhf4It1Bj31cDMCIQCXJEJo\n" +
                "fcC35vuL3NrR0+JhET9UdrsNNqejV/sSMEHM7g==\n" +
                "-----END CERTIFICATE-----").getBytes());
        // chainClientConfig.setUserSignKeyBytes(FileUtils.getResourceFileBytes(CLIENT1_KEY_PATH));
        chainClientConfig.setUserSignKeyBytes(("-----BEGIN EC PRIVATE KEY-----\n" +
                "MHcCAQEEIOqqm1YnQx3nhwzByeCavzjT3Aeh7pCFjHU9sXM+M9JboAoGCCqGSM49\n" +
                "AwEHoUQDQgAEHiBIYo30dm3RjHXe+T0be2IIyQslLOB9yLW+mGpHGyVE6MM8QFOW\n" +
                "DIGaLMIqDjX/R1bOX0gcxCvZLsf9aOfAdA==                            \n" +
                "-----END EC PRIVATE KEY-----").getBytes());
        // chainClientConfig.setUserSignCrtBytes(FileUtils.getResourceFileBytes(CLIENT1_CERT_PATH));
        chainClientConfig.setUserSignCrtBytes(("-----BEGIN CERTIFICATE-----\n" +
                "MIICiDCCAi6gAwIBAgIDDZVJMAoGCCqGSM49BAMCMHIxCzAJBgNVBAYTAmNuMRAw\n" +
                "DgYDVQQIEwdiZWlqaW5nMRAwDgYDVQQHEwdiZWlqaW5nMRMwEQYDVQQKEwpUZXN0\n" +
                "Q01vcmcxMRIwEAYDVQQLEwlyb290LWNlcnQxFjAUBgNVBAMTDWNhLmNtdGVzdG9y\n" +
                "ZzEwHhcNMjMwNDAyMjE0MTU3WhcNMzIwMzMwMjE0MTU3WjB8MQswCQYDVQQGEwJj\n" +
                "bjEQMA4GA1UECBMHYmVpamluZzEQMA4GA1UEBxMHYmVpamluZzETMBEGA1UEChMK\n" +
                "VGVzdENNb3JnMTEOMAwGA1UECxMFYWRtaW4xJDAiBgNVBAMTG2NtdGVzdHVzZXIx\n" +
                "LnNpZ24uVGVzdENNb3JnMTBZMBMGByqGSM49AgEGCCqGSM49AwEHA0IABB4gSGKN\n" +
                "9HZt0Yx13vk9G3tiCMkLJSzgfci1vphqRxslROjDPEBTlgyBmizCKg41/0dWzl9I\n" +
                "HMQr2S7H/WjnwHSjgagwgaUwDgYDVR0PAQH/BAQDAgGmMA8GA1UdJQQIMAYGBFUd\n" +
                "JQAwKQYDVR0OBCIEIANJDIWtvMmJ4fYKX9o//H33fbwINIrjUjGtwcUEy/ECMCsG\n" +
                "A1UdIwQkMCKAIFkfB6E7TRxeWohoBax/cKcGnjQ/s+mYM7DmQj7GY4f6MCoGA1Ud\n" +
                "EQQjMCGCCWxvY2FsaG9zdIIOY2hhaW5tYWtlci5vcmeHBH8AAAEwCgYIKoZIzj0E\n" +
                "AwIDSAAwRQIhALEuDZvrQ1Z7DBEjXgjLz+akTWtsoYAbBFV7jU+DiDofAiAicLjP\n" +
                "r9PyS6o7GaX8xF0dTiwAd0kRwR+Tu52jhW8/Ow==\n" +
                "-----END CERTIFICATE-----").getBytes());
        chainClientConfig.setRpcClient(rpcClientConfig);
        chainClientConfig.setNodes(nodeConfigs);

        if (chainClient == null) {
            chainClient = chainManager.createChainClient(sdkConfig);
        }

        //     adminUser1 = new User(ORG_ID1, FileUtils.getResourceFileBytes(ADMIN1_KEY_PATH),
        //             FileUtils.getResourceFileBytes(ADMIN1_CERT_PATH),
        //             FileUtils.getResourceFileBytes(ADMIN1_TLS_KEY_PATH),
        //             FileUtils.getResourceFileBytes(ADMIN1_TLS_CERT_PATH), true);
        //     adminUser2 = new User(ORG_ID2, FileUtils.getResourceFileBytes(ADMIN2_KEY_PATH),
        //             FileUtils.getResourceFileBytes(ADMIN2_CERT_PATH),
        //             FileUtils.getResourceFileBytes(ADMIN2_TLS_KEY_PATH),
        //             FileUtils.getResourceFileBytes(ADMIN2_TLS_CERT_PATH), true);
        //     adminUser3 = new User(ORG_ID3, FileUtils.getResourceFileBytes(ADMIN3_KEY_PATH),
        //             FileUtils.getResourceFileBytes(ADMIN3_CERT_PATH),
        //             FileUtils.getResourceFileBytes(ADMIN3_TLS_KEY_PATH),
        //             FileUtils.getResourceFileBytes(ADMIN3_TLS_CERT_PATH), true);
    }

    public static void initChainClient() throws Exception {
        Yaml yaml = new Yaml();
        InputStream in = InitClient.class.getClassLoader().getResourceAsStream(SDK_CONFIG);

        SdkConfig sdkConfig;
        sdkConfig = yaml.loadAs(in, SdkConfig.class);
        assert in != null;
        in.close();

        for (NodeConfig nodeConfig : sdkConfig.getChainClient().getNodes()) {
            List<byte[]> tlsCaCertList = new ArrayList<>();
            if (nodeConfig.getTrustRootPaths() != null) {
                for (String rootPath : nodeConfig.getTrustRootPaths()) {
                    List<String> filePathList = FileUtils.getFilesByPath(rootPath);
                    for (String filePath : filePathList) {
                        tlsCaCertList.add(FileUtils.getFileBytes(filePath));
                    }
                }
            }
            byte[][] tlsCaCerts = new byte[tlsCaCertList.size()][];
            tlsCaCertList.toArray(tlsCaCerts);
            nodeConfig.setTrustRootBytes(tlsCaCerts);
        }

        chainManager = ChainManager.getInstance();
        chainClient = chainManager.getChainClient(sdkConfig.getChainClient().getChainId());

        if (chainClient == null) {
            chainClient = chainManager.createChainClient(sdkConfig);
            chainClient.enableCertHash();
        }

        adminUser = new User("root", FileUtils.getResourceFileBytes("crypto-config/user/admin1/SignKey.key"),
                FileUtils.getResourceFileBytes("crypto-config/user/admin1/SignCert.crt"),
                FileUtils.getResourceFileBytes("crypto-config/user/admin1/TlsKey.key"),
                FileUtils.getResourceFileBytes("crypto-config/user/admin1/TlsCert.crt"));

        user1 = new User("user1", FileUtils.getResourceFileBytes("crypto-config/user/testuser01/key.key"),
                FileUtils.getResourceFileBytes("crypto-config/user/testuser01/crt.crt"),
                FileUtils.getResourceFileBytes("crypto-config/user/testuser01/key.key"),
                FileUtils.getResourceFileBytes("crypto-config/user/testuser01/crt.crt"));
    }

}
