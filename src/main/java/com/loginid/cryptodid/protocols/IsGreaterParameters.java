package com.loginid.cryptodid.protocols;

import java.io.Serializable;

public class IsGreaterParameters implements Serializable {
    public MG_FHE.MG_Cipher[] ciphers;
    public MG_FHE.MG_Cipher [] base;
    public int current;
    public MG_FHE fhe;
    int base_balance;
    public IsGreaterParameters(MG_FHE.MG_Cipher[] ciphers, MG_FHE.MG_Cipher[] base, int current, MG_FHE fhe) {
        this.ciphers = ciphers;
        this.base =base;
        this.current = current;
        this.fhe = fhe;

    }
}
