package com.loginid.cryptodid.protocols;

import java.io.Serializable;

public class SetMerkleTreeParameters implements Serializable {
    public boolean [] verification;
    public SetMerkleTreeParameters(boolean [] verification) {
        this.verification = verification;
    }
}
