
package com.sense.util;


import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * SecurityUtil.java
 * Created at 2019-07-12
 * Created by lx
 * Copyright C 20190712 SAIC SHANGHAI VOLKSWAGEN, All rights reserved.
 */
public class SecurityUtil {
	
	private static final Base64 base64encoder = new Base64();
	private static final String encoding = "UTF-8";
	private static byte[] keyData = { 6, 4, 4, 5, 5, 5, 5, 5 };

	protected static Log logger= LogFactory.getLog(SecurityUtil.class);
	public SecurityUtil() {
	}

	public SecurityUtil(int key1, int key2) {
		keyData = new byte[] { 6, 33, 4, 127, 5, 98, 12, 5 };
		keyData[4] = ((byte) key2);
		keyData[6] = ((byte) key1);
	}

	public SecurityUtil(byte[] keyData) {
		this();
		SecurityUtil.keyData = keyData;
	}

	public static int random() {
		int random = (int) (Math.random() * 127.0D);
		return random;
	}

	public static String enCode(String str) {
		String result = str;
		if ((str != null) && (str.length() > 0)) {
			try {
				byte[] encodeByte = symmetricEncrypto(str.getBytes("UTF-8"));
				result = new String(Base64.encode(encodeByte), "UTF-8");
			} catch (Exception e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
		return result;
	}

	public static String deCode(String str) {
		String result = str;
		if ((str != null) && (str.length() > 0)) {
			try {
				byte[] encodeByte = Base64.decode(str.getBytes("UTF-8"));
				byte[] decoder = symmetricDecrypto(encodeByte);
				result = new String(decoder, "UTF-8");
			} catch (Exception e) {
				logger.error(e);
			}
		}
		return result;
	}

	private static byte[] symmetricEncrypto(byte[] byteSource) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			int mode = 1;
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			DESKeySpec keySpec = new DESKeySpec(keyData);
			Key key = keyFactory.generateSecret(keySpec);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(mode, key);
			byte[] result = cipher.doFinal(byteSource);
			return result;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			baos.close();
		}
	}

	private static byte[] symmetricDecrypto(byte[] byteSource) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			int mode = 2;
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			DESKeySpec keySpec = new DESKeySpec(keyData);
			Key key = keyFactory.generateSecret(keySpec);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(mode, key);
			byte[] result = cipher.doFinal(byteSource);
			return result;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			baos.close();
		}
	}

	protected byte[] hashMethod(byte[] byteSource) throws Exception {
		try {
			MessageDigest currentAlgorithm = MessageDigest.getInstance("SHA-1");
			currentAlgorithm.reset();
			currentAlgorithm.update(byteSource);
			return currentAlgorithm.digest();
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

	public static void main(String[] args) {
		String  a= SecurityUtil.enCode("18693117890");
		System.out.println(a);
		System.out.println(SecurityUtil.deCode(a));
	}
}
