/*
Copyright (C) THL A29 Limited, a Tencent company. All rights reserved.

SPDX-License-Identifier: Apache-2.0
*/
package top.ckxgzxa.filestoragesharesystem.sdk;

import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.bouncycastle.util.encoders.Hex;
import org.chainmaker.pb.common.Request;
import org.chainmaker.pb.common.ResultOuterClass;
import org.chainmaker.sdk.SdkException;
import org.chainmaker.sdk.User;
import org.chainmaker.sdk.utils.SdkUtils;
import org.junit.Assert;
import org.junit.Test;

import static top.ckxgzxa.filestoragesharesystem.cmsdk.InitClient.*;


public class TestCertManage {



    @Test
    public void testAddCert() throws Exception {
        initChainClient();
        chainClient.setClientUser(user1);
        ResultOuterClass.TxResponse responseInfo = null;
        try {
            responseInfo = chainClient.addCert(10000);
        } catch (SdkException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        System.out.println(Hex.toHexString(responseInfo.getContractResult().getResult().toByteArray()));
        Assert.assertNotNull(responseInfo);
    }



    // @Test
    // public void testDeleteCert() {
    //     ResultOuterClass.TxResponse responseInfo = null;
    //     try {
    //         //certHash获取方法见testAddCert
    //         chainClient.enableCertHash();
    //         String[] certHashes = new String[]{ByteUtils.toHexString(chainClient.getClientUser().getCertHash())};
    //         Request.Payload payload = chainClient.createCertDeletePayload(certHashes);
    //         Request.EndorsementEntry[] endorsementEntries = SdkUtils.getEndorsers(payload, new User[]{adminUser1, adminUser2, adminUser3});
    //
    //         responseInfo = chainClient.deleteCert(payload, endorsementEntries, rpcCallTimeout, syncResultTimeout);
    //
    //     } catch (SdkException e) {
    //         e.printStackTrace();
    //         Assert.fail(e.getMessage());
    //     }
    //     Assert.assertNotNull(responseInfo);
    // }

    @Test
    public void testQueryCert() throws Exception {
        initChainClient();
        // initWithNoConfig();
        // ChainClient chainClient = new ChainClient();
        ResultOuterClass.CertInfos certInfos = null;
        try {
            chainClient.enableCertHash();
            String[] certHashes = new String[]{ByteUtils.toHexString(chainClient.getClientUser().getCertHash())};
            certInfos = chainClient.queryCert(certHashes,10000);
        } catch (SdkException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        Assert.assertNotNull(certInfos);
        System.out.println(certInfos);
    }

    @Test
    public void testfreezeCerts() throws Exception {
        initChainClient();
        ResultOuterClass.TxResponse responseInfo = null;
        try {
            chainClient.enableCertHash();
            String[] certHashes = new String[]{"-----BEGIN CERTIFICATE-----\n" +
                    "MIICUzCCAfigAwIBAgIIG5ZOML6sDiowCgYIKoZIzj0EAwIwZjELMAkGA1UEBhMC\n" +
                    "Y24xEDAOBgNVBAgTB2JlaWppbmcxEDAOBgNVBAcTB2JlaWppbmcxDTALBgNVBAoT\n" +
                    "BHJvb3QxEjAQBgNVBAsTCXJvb3QtY2VydDEQMA4GA1UEAxMHY2Eucm9vdDAeFw0y\n" +
                    "MzA1MjExODU3NDBaFw0yODA1MTkxODU3NDBaMGYxCzAJBgNVBAYTAkNOMRAwDgYD\n" +
                    "VQQIEwdCZWlKaW5nMRAwDgYDVQQHEwdCZWlKaW5nMQ0wCwYDVQQKEwRyb290MQ8w\n" +
                    "DQYDVQQLEwZjbGllbnQxEzARBgNVBAMTCnRlc3R1c2VyMDMwWTATBgcqhkjOPQIB\n" +
                    "BggqhkjOPQMBBwNCAAQQZyZ6aJmwkbrBQIlBtnxnhuqYKo4YIKGHdC2RyQb+hGJM\n" +
                    "THWu+G9gOd+ud5F/KNsM6Fy9vc0aUO0m4YLD38eDo4GPMIGMMA4GA1UdDwEB/wQE\n" +
                    "AwIGwDATBgNVHSUEDDAKBggrBgEFBQcDAjApBgNVHQ4EIgQgOfFOj49yt/Xw5FIk\n" +
                    "qjLVvRIAQnCox6J6zm3eDjmiiXkwKwYDVR0jBCQwIoAgHNKYxHr5BtQ3CxbtnmGo\n" +
                    "Cq3lOKbtWbQQ9A99OsKhzOIwDQYDVR0RBAYwBIIAggAwCgYIKoZIzj0EAwIDSQAw\n" +
                    "RgIhAIW8I5FssvKd71zE4dKnnRqBTd7jgpNRD89Wr92X7hYcAiEA0X9HoSrGBham\n" +
                    "cOLW8/9F3r5xvEtWLFAeqhlNSM8r2OE=\n" +
                    "-----END CERTIFICATE-----\n"};
            Request.Payload payload = chainClient.createCertFreezePayload(certHashes);

            Request.EndorsementEntry[] endorsementEntries = SdkUtils.getEndorsers(payload, new User[]{user1});

            responseInfo = chainClient.freezeCerts(payload, endorsementEntries, 10000, 10000);

        } catch (SdkException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        System.out.println(responseInfo);
        Assert.assertNotNull(responseInfo);
    }

    @Test
    public void testUnfreezeCerts() throws Exception {
        initChainClient();
        ResultOuterClass.TxResponse responseInfo = null;
        try {
            chainClient.enableCertHash();
            String[] certHashes = new String[]{"-----BEGIN CERTIFICATE-----\n" +
                    "MIICUjCCAfigAwIBAgIIPLQ9fqIR8JYwCgYIKoZIzj0EAwIwZjELMAkGA1UEBhMC\n" +
                    "Y24xEDAOBgNVBAgTB2JlaWppbmcxEDAOBgNVBAcTB2JlaWppbmcxDTALBgNVBAoT\n" +
                    "BHJvb3QxEjAQBgNVBAsTCXJvb3QtY2VydDEQMA4GA1UEAxMHY2Eucm9vdDAeFw0y\n" +
                    "MzA1MjExODUxNDNaFw0yODA1MTkxODUxNDNaMGYxCzAJBgNVBAYTAkNOMRAwDgYD\n" +
                    "VQQIEwdCZWlKaW5nMRAwDgYDVQQHEwdCZWlKaW5nMQ0wCwYDVQQKEwRyb290MQ8w\n" +
                    "DQYDVQQLEwZjbGllbnQxEzARBgNVBAMTCnRlc3R1c2VyMDEwWTATBgcqhkjOPQIB\n" +
                    "BggqhkjOPQMBBwNCAATMwgzBwSwjw1h0h7VzJ4oubONBUydQIw1BCmpl3Jtvqdh8\n" +
                    "+55rvbKhe9nK7Nrgb+E6DwOeVv0C1G6vMz2RRw90o4GPMIGMMA4GA1UdDwEB/wQE\n" +
                    "AwIGwDATBgNVHSUEDDAKBggrBgEFBQcDAjApBgNVHQ4EIgQga8Bi65rMrXbP95T7\n" +
                    "9epDyLf0y8ULaJqIFTRQxIz8MhgwKwYDVR0jBCQwIoAgHNKYxHr5BtQ3CxbtnmGo\n" +
                    "Cq3lOKbtWbQQ9A99OsKhzOIwDQYDVR0RBAYwBIIAggAwCgYIKoZIzj0EAwIDSAAw\n" +
                    "RQIgDld7ZzOzVDbZOnQZi9WgDJ7FoRkAIh2tWMyeYH0/BaYCIQCywGUtyqOq/pQl\n" +
                    "SDaD4XzbWpcQtqQQmR9CTzqOHDgBXw==\n" +
                    "-----END CERTIFICATE-----\n"};
            Request.Payload payload = chainClient.createPayloadOfUnfreezeCerts(certHashes);

            Request.EndorsementEntry[] endorsementEntries = SdkUtils.getEndorsers(payload, new User[]{user1});

            responseInfo = chainClient.unfreezeCerts(payload, endorsementEntries, 10000, 10000);
            System.out.println(responseInfo);
        } catch (SdkException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        Assert.assertNotNull(responseInfo);
    }

    // @Test
    // public void testRevokeCerts() {
    //     ResultOuterClass.TxResponse responseInfo = null;
    //     try {
    //         chainClient.enableCertHash();
    //         Request.Payload payload = chainClient.createPayloadOfRevokeCerts(new String(FileUtils.getResourceFileBytes(CLIENT_CRL_PATH)));
    //
    //         Request.EndorsementEntry[] endorsementEntries = SdkUtils.getEndorsers(payload, new User[]{adminUser1, adminUser2, adminUser3});
    //
    //         responseInfo = chainClient.revokeCerts(payload, endorsementEntries, rpcCallTimeout, syncResultTimeout);
    //     } catch (SdkException e) {
    //         e.printStackTrace();
    //         Assert.fail(e.getMessage());
    //     }
    //     Assert.assertNotNull(responseInfo);
    // }
}
