package com.loginid.cryptodid.protocols;

import com.loginid.cryptodid.model.Claim;

import java.io.Serializable;

public class ProofParameters implements Serializable {
    public Claim claim;
    public MG_FHE fhe;
    public int base_balance;
    public ProofParameters(Claim claim, MG_FHE fhe, int base_balance) {
        this.claim = claim;
        this.fhe = fhe;
        this.base_balance =base_balance;
    }
}
