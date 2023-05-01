package com.loginid.cryptodid.protocols;

import com.loginid.cryptodid.model.Claim;
import com.loginid.cryptodid.protocols.*;

import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;


public class Verifier {
    public static int index;

    public static MG_FHE.MG_Cipher[] verifySIN(Claim sinVC, MG_FHE fhe){
        int LIST_SIZE = 5;
        int [] exclusionList = {111111111,222222222,333333333,444444444,555555555};
        MG_FHE.MG_Cipher [] excList = new MG_FHE.MG_Cipher[LIST_SIZE];
        for (int i = 0; i < LIST_SIZE; i++) {
            excList[i] = fhe.encrypt(new BigInteger(""+exclusionList[i],10));
        }

        MG_FHE.MG_Cipher sinCipher = sinVC.ciphers;
        MG_FHE.MG_Cipher A = fhe.ONE;
        for (int k = 0; k < LIST_SIZE; k++) {
            A = A.mult(sinCipher.sub(excList[k]));
        }
        //Generate the prime number
        Random rnd = new Random();
        MG_FHE.MG_Cipher [] P = new MG_FHE.MG_Cipher [1000];
        for (int i = 0 ; i < 1000; i++) {
            double r1 = Math.random();
            if (r1<0.5) {
                P[i] = fhe.encrypt(new BigInteger("0",10));
            } else {

                P[i] = fhe.encrypt(BigInteger.probablePrime(fhe.lambda / 2,

                        rnd).mod(BigInteger.probablePrime(fhe.lambda / 2, rnd)));
            }
        }
        index = (int) (1000*Math.random());
        P[index] = A;
        return P;
    }

    public static String[] statuteOnProverResponse(BigInteger[] R){
        String[] responseToSend = new String[3];
        BigInteger r = R[index];
        responseToSend[0] = String.valueOf(!r.equals(BigInteger.ZERO));
        if (!r.equals(BigInteger.ZERO)) {
            responseToSend[1] = "You are authorized to access the building";
        } else {
            responseToSend[1] ="You are NOT authorized to access the building";
        }
        return responseToSend;
    }



}