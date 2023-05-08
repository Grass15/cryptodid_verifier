package com.loginid.cryptodid.protocols;


public class TfheVerifier {

    static {
        System.loadLibrary("native");
    }
    public TfheVerifier(){}
    public static void main(String[] args) {
        System.out.println(new TfheVerifier().verifyAge());
    }

    // Declare a native method sayHello() that receives no arguments and returns void
    public native String verifyAge();
    public native String verifyBalance();
    public native String CreditScore();
}