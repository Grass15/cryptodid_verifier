package com.loginid.cryptodid.protocols;

import java.io.Serializable;

public class Proof implements Serializable{
    public MG_FHE.MG_Cipher [] L;
    public int proof_index;
    public MG_FHE.MG_Cipher CK;
    public MG_FHE.MG_Cipher [] base;
    public boolean [] verification;
    public Proof(int size, int h) {
        L =new MG_FHE.MG_Cipher[size];
        base = new MG_FHE.MG_Cipher[h];
        verification = new boolean[size];
    }

}
