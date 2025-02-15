package top.ckxgzxa.filestoragesharesystem.service.impl;

import org.chainmaker.sdk.utils.CryptoUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.ckxgzxa.filestoragesharesystem.service.FileService;

import javax.annotation.Resource;

import static top.ckxgzxa.filestoragesharesystem.cmsdk.InitClient.initChainClient;

/**
 * @author 赵希奥
 * @date 2023/5/25 5:42
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */

@SpringBootTest
class FileServiceImplTest {

    @Resource
    private FileService fileService;

    @Test
    void checkFileAccess() throws Exception {
        initChainClient();

        Boolean b = fileService.checkFileAccess(6L, 24L);
        System.out.println(b);
    }

    @Test
    void test() {
        System.out.println(CryptoUtils.getEVMAddressFromCertBytes(("-----BEGIN CERTIFICATE-----\n" +
                        "MIICTTCCAfKgAwIBAgIIBadi66f8kmcwCgYIKoZIzj0EAwIwZjELMAkGA1UEBhMC\n" +
                        "Y24xEDAOBgNVBAgTB2JlaWppbmcxEDAOBgNVBAcTB2JlaWppbmcxDTALBgNVBAoT\n" +
                        "BHJvb3QxEjAQBgNVBAsTCXJvb3QtY2VydDEQMA4GA1UEAxMHY2Eucm9vdDAeFw0y\n" +
                        "MzA1MzAxNTIyMjhaFw0yODA1MjgxNTIyMjhaMGAxCzAJBgNVBAYTAkNOMRAwDgYD\n" +
                        "VQQIEwdCZWlKaW5nMRAwDgYDVQQHEwdCZWlKaW5nMQ0wCwYDVQQKEwRyb290MQ4w\n" +
                        "DAYDVQQLEwVhZG1pbjEOMAwGA1UEAxMFYWRtaW4wWTATBgcqhkjOPQIBBggqhkjO\n" +
                        "PQMBBwNCAASbFVqPHZq5p6MhCPqx6jVK9KuR+EIjOxG9RDTbaH1CfT5gWfg2bOHT\n" +
                        "8eXD1AgbAx6Ir+K5SKk987sRffkLWfiDo4GPMIGMMA4GA1UdDwEB/wQEAwIGwDAT\n" +
                        "BgNVHSUEDDAKBggrBgEFBQcDAjApBgNVHQ4EIgQgUcaee2nMdDtr9Y0WH8m/3ZeS\n" +
                        "T3Y8MZYQ/7ugvxC4SmQwKwYDVR0jBCQwIoAgHNKYxHr5BtQ3CxbtnmGoCq3lOKbt\n" +
                        "WbQQ9A99OsKhzOIwDQYDVR0RBAYwBIIAggAwCgYIKoZIzj0EAwIDSQAwRgIhANSl\n" +
                        "jWCnhXEjzZFwsMwKIR1TYbs/da0t8z5WsFlm1R5UAiEAowgCOcj6OTk8n7r7WNz7\n" +
                        "8HGRbT4aAxNULwkrRotjm7s=\n" +
                        "-----END CERTIFICATE-----").getBytes()));
    }
}