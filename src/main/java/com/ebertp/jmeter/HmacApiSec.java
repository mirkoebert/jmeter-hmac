package com.ebertp.jmeter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


public final class HmacApiSec {

	private Mac sha256_HMAC;

	public HmacApiSec(String secret) throws NoSuchAlgorithmException, InvalidKeyException{
		sha256_HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
		sha256_HMAC.init(secret_key);

	}
	
	public String getHmacFromMessage(String httpBody, String fullURL, String httpMethod){
		String hash = Base64.encodeBase64String(sha256_HMAC.doFinal(httpBody.getBytes()));
		System.out.println(hash);
		long d = System.currentTimeMillis();
		String newMessage = hash + fullURL + httpMethod + d;
		System.out.println(newMessage);
		String hash2 = Base64.encodeBase64String(sha256_HMAC.doFinal(newMessage.getBytes()));
		return hash2;
		
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
		String finalHmac = (new HmacApiSec("secretXXX")).getHmacFromMessage("MEMEME", "https://ebert-p.com", "GET");
		System.out.println(finalHmac);
	}
}
