package com.ebertp.jmeter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


public final class ApiSec {

	private Mac sha256_HMAC;

	public ApiSec(String secret) throws NoSuchAlgorithmException, InvalidKeyException{
		sha256_HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
		sha256_HMAC.init(secret_key);
	}
	
	public String getHmacFromMessage(final String httpBody, String fullURL, String httpMethod){
		String hash = Base64.encodeBase64String(sha256_HMAC.doFinal(httpBody.getBytes()));
		System.out.println(hash);
		long d = System.currentTimeMillis();
		String newMessage = hash + fullURL + httpMethod + d;
		System.out.println(newMessage);
		return Base64.encodeBase64String(sha256_HMAC.doFinal(newMessage.getBytes()));
		
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
		String finalHmac = (new ApiSec("secretXXX")).getHmacFromMessage("MEMEME", "https://ebert-p.com", "GET");
		System.out.println(finalHmac);
	}
}
