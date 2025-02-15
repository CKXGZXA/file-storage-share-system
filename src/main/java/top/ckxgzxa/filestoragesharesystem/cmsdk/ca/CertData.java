package top.ckxgzxa.filestoragesharesystem.cmsdk.ca;

/**
 * @author 赵希奥
 * @date 2023/4/13 21:40
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */
public class CertData {
    private String certSn;
    private String issueCertSn;
    private String cert;
    private String privateKey;

    public String getCertSn() {
        return certSn;
    }

    public void setCertSn(String certSn) {
        this.certSn = certSn;
    }

    public String getIssueCertSn() {
        return issueCertSn;
    }

    public void setIssueCertSn(String issueCertSn) {
        this.issueCertSn = issueCertSn;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}

