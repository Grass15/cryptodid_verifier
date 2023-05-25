package com.loginid.cryptodid.model;


import com.loginid.cryptodid.protocols.MG_FHE;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.Date;

public class Claim implements Serializable {

	String title;
	int id;
	String type;
	String issuerName;
	String content;
	String expirationDate;
	public MG_FHE.MG_Cipher [] ciphers;
	String issuingDate;
	MG_FHE.MG_Cipher [] PK;
	MG_FHE fhe;
	int hash; //plays the role of a signature

	public Claim(String title, String type, String issuerName, String content) {
		ciphers = new MG_FHE.MG_Cipher[8];
		this.title = title;
		this.type = type;
		this.issuerName = issuerName;
		this.content = content;
	}
	public int getHash() {
		return hash;
	}
	public void setHash(int hash) {
		this.hash = hash;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public MG_FHE.MG_Cipher[] getPK() {
		return PK;
	}
	public MG_FHE getFhe() {
		return fhe;
	}
	public void setPK(MG_FHE.MG_Cipher[] PK) {
		this.PK = PK;
	}
	public void setFhe(MG_FHE fhe) {
		this.fhe = fhe;
	}
	public String getIssuingDate() {
		return issuingDate;
	}
	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String date) {
		this.expirationDate = date;
	}

	public void setIssuingDate(String date) {
		this.issuingDate = date;
	}
	public MG_FHE.MG_Cipher[] getCiphers() {
		return ciphers;
	}

	public byte[] ciphersToByteArray(MG_FHE.MG_Cipher[] ciphers) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(ciphers);
		return baos.toByteArray();
	}
	public MG_FHE.MG_Cipher[] byteArrayToCiphers(byte[] ciphersByteArray) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(ciphersByteArray);
		ObjectInputStream ois = new ObjectInputStream(bais);
		return (MG_FHE.MG_Cipher[]) ois.readObject();
	}

	public byte[] fheToByteArray(MG_FHE fhe) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(fhe);
		return baos.toByteArray();
	}
	public MG_FHE byteArrayToFhe(byte[] fheByteArray) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(fheByteArray);
		ObjectInputStream ois = new ObjectInputStream(bais);
		return (MG_FHE) ois.readObject();
	}

	public byte[] PKToByteArray(MG_FHE.MG_Cipher[] PK) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(PK);
		return baos.toByteArray();
	}
	public MG_FHE.MG_Cipher[] byteArrayToPK(byte[] PKByteArray) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(PKByteArray);
		ObjectInputStream ois = new ObjectInputStream(bais);
		return (MG_FHE.MG_Cipher[]) ois.readObject();
	}
	public void setCiphers(MG_FHE.MG_Cipher[] ciphers) {
		this.ciphers = ciphers;
	}
	public String getTitle() {
		return title;
	}
	public String getType() {
		return type;
	}
	public String getIssuerName() {
		return issuerName;
	}
	public String getContent() {
		return content;
	}

	@Override
	public String toString() {
		return super.toString() + this.issuerName + this.type + this.id;
	}
}
