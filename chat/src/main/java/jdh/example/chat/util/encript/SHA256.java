package jdh.example.chat.util.encript;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SHA256 {

	private static Logger logger = LoggerFactory.getLogger(SHA256.class);

	public static String getSalt() {
		java.util.Random random = new java.util.Random();
		byte[] saltBytes = new byte[8];
		random.nextBytes(saltBytes);
		
		StringBuffer salt = new StringBuffer();
		for (int i = 0; i < saltBytes.length; i++)
		{
			salt.append(String.format("%02x",saltBytes[i]));
		}
		return salt.toString();
	}
	
	public static String encrypt(String SrcText,String salt) {
		String returnStr = "";
		MessageDigest msgDigest;
		try 
		{
			msgDigest = MessageDigest.getInstance("SHA-256");
			msgDigest.reset();
			msgDigest.update(salt.getBytes());
			byte[] digest = msgDigest.digest(SrcText.getBytes());
			returnStr = Hex.encodeHexString(digest);
		} catch (NoSuchAlgorithmException e) {
			logger.error("[SHA256]에서 익셉션 발생 - " + e.getMessage());
		}
		return returnStr;
	}
	
	public static String encrypt(String SrcText) {
		String returnStr = "";
		MessageDigest msgDigest;
		try 
		{
			msgDigest = MessageDigest.getInstance("SHA-256");
			msgDigest.reset();
			byte[] digest = msgDigest.digest(SrcText.getBytes());
			returnStr = Hex.encodeHexString(digest);
		} catch (NoSuchAlgorithmException e) {
			logger.error("[SHA256]에서 익셉션 발생 - " + e.getMessage());
		}
		return returnStr;
	}
}
