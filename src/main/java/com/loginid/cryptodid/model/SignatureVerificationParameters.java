package com.loginid.cryptodid.model;

import java.io.Serializable;


public class SignatureVerificationParameters implements Serializable {
    public byte[] vcEncryptedData;
    public byte[] signatureBytes;
    public byte[] certificateBytes;
    public byte[] revocationNonce;
    public int version;

    public SignatureVerificationParameters(byte[] vcEncryptedData, byte[] signatureBytes, byte[] certificateBytes, byte[] revocationNonce, int version) {
        this.vcEncryptedData = vcEncryptedData;
        this.signatureBytes= signatureBytes;
        this.certificateBytes = certificateBytes;
        this.revocationNonce = revocationNonce;
        this.version = version;
    }

}
