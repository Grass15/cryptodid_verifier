package com.loginid.cryptodid.protocols;

import java.math.BigInteger;
import java.util.Random;
import java.io.Serializable;

public class MG_FHE implements Serializable {

	final static int LAMBDA = 512;
	final static int H = 11;
	final static int PK_SIZE = 256;
	public class MG_Cipher implements Serializable {
		public BigInteger[] alphas;
		MG_Cipher() {
			alphas = new BigInteger[11];
		}
		public MG_Cipher add(MG_Cipher a, BigInteger N) {
			MG_Cipher c = new MG_Cipher();
			for (int i = 0; i < 11; i++) {
				c.alphas[i] = ((this.alphas[i]).add(a.alphas[i])).mod(N);
			}
			return c;
		}
		public MG_Cipher sub(MG_Cipher a, int h, BigInteger N) {
			MG_Cipher c = new MG_Cipher();
			for (int i = 0; i < h; i++) {
				c.alphas[i] = ((this.alphas[i]).subtract(a.alphas[i])).mod(N);
			}
			return c;
		}
		public MG_Cipher mult(BigInteger a, BigInteger N) {
			MG_Cipher c = new MG_Cipher();
			for (int i = 0; i < 11; i++) {
				c.alphas[i] = ((this.alphas[i]).multiply(a)).mod(N);
			}
			return c;
		}
		public MG_Cipher mult(MG_Cipher a, BigInteger N, BigInteger [][][] X) {
			MG_Cipher c = new MG_Cipher();
			for (int i = 0; i < 11; i++) {
				c.alphas[i] = BigInteger.ZERO;
				for (int j = 0; j < 11; j++) {
					for (int k = 0; k < 11; k++) {
						c.alphas[i] = ((c.alphas[i]).add(((this.alphas[j]).multiply(a.alphas[k])).multiply(X[j][k][i]))).mod(N);
					}
				}
			}
			return c;
		}
		public boolean is_equal(MG_Cipher a){
			for (int i = 0; i < 11; i++) {
				if (!this.alphas[i].equals(a.alphas[i])) {
					return false;
				}
			}
			return true;
		}
	}
	public BigInteger [][][] X;
	public BigInteger N;
	public int h, lambda;
	BigInteger [] sk;
	BigInteger p,q;
	Random rnd;
	public MG_Cipher ONE;
	public MG_Cipher ZERO;
	public MG_Cipher [] PK;

	public static void main(String[] arguments) {
		MG_FHE fhe = new MG_FHE(H,LAMBDA);
		BigInteger m1 = new BigInteger("500", 10);
		BigInteger m2 = new BigInteger("700", 10);
		MG_Cipher C1 = fhe.encrypt_private(m1);
		MG_Cipher C2 = fhe.encrypt_private(m2);
		MG_Cipher C = C1.add(C2, fhe.N);
		BigInteger m = fhe.decrypt(C);
		System.out.println(m.toString());
	}
	public MG_FHE(int h, int lambda){
		this.rnd = new Random();
		this.lambda = lambda;
		this.h = h;
		this.p = BigInteger.probablePrime(lambda, this.rnd);
		this.q = BigInteger.probablePrime(lambda, this.rnd);
		this.N = this.p.multiply(this.q);
		sk = new BigInteger[this.h];
		X = new BigInteger[this.h][this.h][this.h];
		//Create secret key
		for (int i = 0; i < h; i++) {
			sk [i] = new BigInteger(lambda, this.rnd);
		}
		//Create public matrix X
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < h; j++) {
				BigInteger Somme = BigInteger.ZERO;
				for (int k = 0; k < h-1; k++) {
					X[i][j][k] = new BigInteger(lambda, rnd);
					Somme = Somme.add(X[i][j][k].multiply(sk[k]));
				}
				X[i][j][h-1] = (((sk[i].multiply(sk[j])).subtract(Somme)).multiply((sk[h-1]).modInverse(this.p))).mod(this.p);
			}
		}
		//Calculate ZERO
		ZERO = encrypt_private(new BigInteger("0",10));
		ONE = encrypt_private(new BigInteger("1",10));
		//Generate the public key
		PK = new MG_Cipher[PK_SIZE];
		for (int i = 0; i < PK_SIZE; i++) {
			PK[i] = encrypt_private(new BigInteger("0",10));
		}
	}
	private MG_Cipher encrypt_private(BigInteger m) {
		MG_Cipher C = new MG_Cipher();
		BigInteger Somme = BigInteger.ZERO;
		for (int k = 0; k < h-1; k++) {
			C.alphas[k] = new BigInteger((this.lambda+this.lambda), rnd);
			Somme = Somme.add(C.alphas[k].multiply(sk[k]));
		}
		C.alphas[h-1] = ((m.subtract(Somme)).multiply((sk[h-1]).modInverse(this.N))).mod(this.N);
		return C;
	}
	public MG_Cipher encrypt(BigInteger m) {
		MG_Cipher C = new MG_Cipher();
		System.out.println(N);
		C = ONE.mult(m, this.N);
		for (int i = 0; i < PK_SIZE; i++) {
			double r = Math.random();
			if (r<0.5) {
				C = C.add(ZERO, N);
			}
		}
		return C;
	}

	public BigInteger decrypt(MG_Cipher C) {
		BigInteger m = BigInteger.ZERO;
		for (int i = 0; i <h; i++) {
			m = (m.add(C.alphas[i].multiply(sk[i]))).mod(this.p);
		}
		return m;
	}
}




