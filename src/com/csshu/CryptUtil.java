package com.csshu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptUtil {

	public static final String encrypt_mode = "encrypt";
	public static final String decrypt_mode = "decrypt";

	public static String checkMd5(byte[] origin) {
		if (origin == null){
			System.out.println("the origin byte is null");
			return null;
		}
		byte[] ret = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			ret = md5.digest(origin);

			StringBuilder hexString = new StringBuilder();
			for (int i = 0; i < ret.length; i++) {
				String h = Integer.toHexString(0xFF & ret[i]);
				while (h.length() < 2) {
					h = "0" + h;
				}
				hexString.append(h);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String checkMd5(File f) {

		if (!f.exists()) {
			System.out.println("the origin file is not exists in checkMd5");
			return null;
		}

		FileInputStream fis = null;
		byte[] rb = null;
		DigestInputStream digestInputStream = null;
		try {
			fis = new FileInputStream(f);
			MessageDigest md5 = MessageDigest.getInstance("md5");
			digestInputStream = new DigestInputStream(fis, md5);
			byte[] buffer = new byte[4096];
			while (digestInputStream.read(buffer) > 0)
				;
			md5 = digestInputStream.getMessageDigest();
			rb = md5.digest();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
					fis = null;
				}
				if (digestInputStream != null) {
					digestInputStream.close();
					digestInputStream = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rb.length; i++) {
			String a = Integer.toHexString(0XFF & rb[i]);
			if (a.length() < 2) {
				a = '0' + a;
			}
			sb.append(a);
		}
		return sb.toString();
	}

	private static void encrypt(InputStream in, OutputStream out, String key,
			String iv) throws Exception {

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(Charset
				.forName("UTF-8")), "AES");
		cipher.init(Cipher.ENCRYPT_MODE, keySpec,
				new IvParameterSpec(iv.getBytes(Charset.forName("utf-8"))));

		out = new CipherOutputStream(out, cipher);
		byte[] buf = new byte[4096];
		int numRead = 0;
		while ((numRead = in.read(buf)) >= 0) {
			out.write(buf, 0, numRead);
		}
		if (in != null) {
			in.close();
		}
		if (out != null) {
			out.close();
		}
	}

	private static void decrypt(InputStream in, OutputStream out, String key,
			String iv) throws Exception {

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(Charset
				.forName("UTF-8")), "AES");
		cipher.init(Cipher.DECRYPT_MODE, keySpec,
				new IvParameterSpec(iv.getBytes(Charset.forName("utf-8"))));

		in = new CipherInputStream(in, cipher);
		byte[] buf = new byte[4096];
		int numRead = 0;
		while ((numRead = in.read(buf)) >= 0) {
			out.write(buf, 0, numRead);
		}
		if (in != null) {
			in.close();
		}
		if (out != null) {
			out.close();
		}
	}

	public static String encryptFile(String originPath, String dstPath,
			String key, String iv) throws Exception {
		File srcFile = new File(originPath);
		if (!srcFile.exists()) {
			System.out.println("src file is not exists");
			return null;
		}
		FileInputStream fis = new FileInputStream(srcFile);
		File encryptFile = new File(dstPath);
		FileOutputStream fos = new FileOutputStream(encryptFile);

		encrypt(fis, fos, key, iv);

		if (fis != null) {
			fis.close();
			fis = null;
		}
		if (fos != null) {
			fos.close();
			fos = null;
		}
		if (encryptFile.exists()) {
			return encryptFile.getAbsolutePath();
		}
		return null;
	}

	public static String decryptFile(String encryptPath, String dstPath,
			String key, String iv) throws Exception {
		File srcFile = new File(encryptPath);
		if (!srcFile.exists()) {
			System.out.println("encrypt file is not exists");
			return null;
		}
		FileInputStream fis = new FileInputStream(srcFile);
		File decryptFile = new File(dstPath);
		FileOutputStream fos = new FileOutputStream(decryptFile);

		decrypt(fis, fos, key, iv);

		if (fis != null) {
			fis.close();
			fis = null;
		}
		if (fos != null) {
			fos.close();
			fos = null;
		}
		if (decryptFile.exists()) {
			return decryptFile.getAbsolutePath();
		}
		return null;
	}

	private static byte[] encrypt(byte[] originData, String key, String iv)
			throws Exception {

		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(Charset
					.forName("UTF-8")), "AES");
			cipher.init(Cipher.ENCRYPT_MODE, keyspec,
					new IvParameterSpec(iv.getBytes(Charset.forName("utf-8"))));

			byte[] encrypted = cipher.doFinal(originData);

			return encrypted;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static byte[] decrypt(byte[] encryptData, String key, String iv)
			throws Exception {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(Charset
					.forName("UTF-8")), "AES");
			cipher.init(Cipher.DECRYPT_MODE, keyspec,
					new IvParameterSpec(iv.getBytes(Charset.forName("utf-8"))));
			byte[] original = cipher.doFinal(encryptData);
			return original;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
