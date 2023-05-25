package com.loginid.cryptodid.protocols;

import com.loginid.cryptodid.model.Claim;

import java.io.Serializable;
import java.security.cert.X509Certificate;

public class ProofParameters implements Serializable {
    public Claim claim;
    public MG_FHE fhe;
    public byte[] signatureBytes;
    public byte[] certificateBytes;
    public ProofParameters(Claim claim, MG_FHE fhe, byte[] signatureBytes,byte[] certificateBytes) {
        this.claim = claim;
        this.fhe = fhe;
        this.signatureBytes= signatureBytes;
        this.certificateBytes = certificateBytes;
    }
}
