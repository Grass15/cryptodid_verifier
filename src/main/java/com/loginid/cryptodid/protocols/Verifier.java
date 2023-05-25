package com.loginid.cryptodid.protocols;

import com.loginid.cryptodid.model.Claim;
import com.loginid.cryptodid.protocols.*;

import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;


public class Verifier {
    public int proof_index;
    int hash;
    int verificationPort;
    int attributeMinimumValue;

    Boolean status;

    public static MG_FHE.MG_Cipher is_greater1(MG_FHE.MG_Cipher[] C1, MG_FHE.MG_Cipher[] C2, int current, MG_FHE fhe) {
        //S[n] = (C1[n]*(C2[n]+1)+(C1[N]+C2[N]+1)*S[n-1]
        if (current == 0) {
            MG_FHE.MG_Cipher t0 = C1[0].mult(C2[0].add(fhe.ONE, fhe.h, fhe.N), fhe.h, fhe.N, fhe.X);
            return t0;
        } else {
            MG_FHE.MG_Cipher t1 = (C1[current]).mult((C2[current]).add(fhe.ONE, fhe.h, fhe.N), fhe.h, fhe.N, fhe.X);
            MG_FHE.MG_Cipher t2 = (C1[current]).add((C2[current]).add(fhe.ONE, fhe.h, fhe.N), fhe.h, fhe.N);
            MG_FHE.MG_Cipher t3 = is_greater1(C1, C2, current - 1, fhe);
            MG_FHE.MG_Cipher t4 = t2.mult(t3, fhe.h, fhe.N, fhe.X);
            MG_FHE.MG_Cipher t5 = t1.add(t4, fhe.h, fhe.N);
            return (t5);
        }
    }

    public static Proof verify(Claim claim, MG_FHE fhe, int attributeMinimumValue,byte[] signatureBytes, String base64Certificate) throws Exception {
        Proof proof = new Proof(1000, fhe.h);
        // 4. Verify signature TBD
        int hashCode = claim.getHash();
        System.out.println("signature verification : "+verifySignature(claim,signatureBytes,base64Certificate));
        if (!verifySignature(claim, signatureBytes, base64Certificate)){
            System.out.println("signature verification faild");
            return null;
        }
        //5. Blindly Verify claim

        MG_FHE.MG_Cipher[] base = new MG_FHE.MG_Cipher[8];
        for (int i = 0; i < 8; i++) {
            base[i] = fhe.encrypt(new BigInteger("" + ((attributeMinimumValue >> i) & 0x1), 10));
            proof.base[i] = base[i];
        }
        MG_FHE.MG_Cipher er;
        er = is_greater1(claim.ciphers, base, 7, fhe); //er is is true if odd, false if even
        // Generate 1000 large odd numbers
        Random rnd = new Random();
        for (int i = 0; i < 1000; i++) {
            BigInteger K = BigInteger.probablePrime(fhe.lambda / 2, rnd);
            double r = Math.random();
            if (r < 0.5) {
                //Make it even
                K = K.add(new BigInteger("1", 10));
            }
            MG_FHE.MG_Cipher CK = fhe.encrypt(K);
            proof.L[i] = CK;
        }
        proof.proof_index = (int) (1000 * (Math.random()));
        BigInteger K = BigInteger.probablePrime(fhe.lambda / 2, rnd);
        K = K.add(new BigInteger("1", 10));
        MG_FHE.MG_Cipher CK = fhe.encrypt(K);
        proof.CK = CK;
        proof.L[proof.proof_index] = er.add(CK, fhe.h, fhe.N);
        return proof;
    }

    public int getProofIndex(int h) {
        hash = h;
        return proof_index;
    }

    public static boolean verifySignature(Claim claim, byte[] signatureBytes, String base64Certificate) throws Exception {
        // Create a Signature object and initialize it with the public key from the certificate
        byte[] certificateBytes = Base64.getDecoder().decode(base64Certificate);

        // Create an X509Certificate object from the byte array
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(certificateBytes));
        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(certificate);
        byte[] claimBytes = serialize(claim);
        verifier.update(claimBytes);

        // Verify the signature
        return verifier.verify(signatureBytes);
    }

    private static byte[] serialize(Claim claim) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(claim);
        return out.toByteArray();
    }


    public Boolean getStatus() {
        return status;
    }

    public static String[] setMerkleTree(boolean[] verification, int hash, int proof_index) { //age_attribute for display only
        String[] response = new String[3];
        int hash2 = Arrays.hashCode(verification);

        if (hash == hash2) {
            response[0] = "Prover is Honest";
        } else {
            response[0] = "Prover is Dishonest";
        }
        //status = verification[proof_index];
        if (verification[proof_index]) {
            response[1] = "Verification positive for this attribute";
        } else {
            //response[1] ="Verification Negative (" + age_attribute + " <= " + this.attributeMinimumValue + ")";
            response[1] = "Verification negative for this attribute";
        }
        response[2] = String.valueOf(verification[proof_index]);
        System.out.println(response[0]);
        System.out.println(response[1]);
        //return response;
        return response;
    }

}