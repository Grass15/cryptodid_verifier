package com.loginid.cryptodid.protocols;

import java.io.Serializable;

public class SetMerkleTreeParameters implements Serializable {
    public boolean [] verification;
    public int hash;
    public int proof_index;
    public SetMerkleTreeParameters(boolean [] verification, int hash, int proof_index) {
        this.verification = verification;
        this.hash = hash;
        this.proof_index = proof_index;
    }
}
