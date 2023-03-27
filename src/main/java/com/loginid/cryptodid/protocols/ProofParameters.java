package com.loginid.cryptodid.protocols;

import com.loginid.cryptodid.model.Claim;

import java.io.Serializable;

public class ProofParameters implements Serializable {
    public Claim claim;
    public MG_FHE fhe;
    public ProofParameters(Claim claim, MG_FHE fhe) {
        this.claim = claim;
        this.fhe = fhe;
    }
}
