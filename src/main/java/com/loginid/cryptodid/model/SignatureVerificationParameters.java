package com.loginid.cryptodid.model;

import java.io.Serializable;


public class SignatureVerificationParameters implements Serializable {
    public byte[] vcEncryptedData;
    public byte[] signatureBytes;
    public byte[] certificateBytes;

    public SignatureVerificationParameters(byte[] vcEncryptedData, byte[] signatureBytes, byte[] certificateBytes) {
        this.vcEncryptedData = vcEncryptedData;
        this.signatureBytes= signatureBytes;
        this.certificateBytes = certificateBytes;
    }
}
