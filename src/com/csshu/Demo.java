package com.csshu;

import java.io.File;

import com.helper.AppLogger;
import com.helper.Config;

public class Demo {
	private final static String key = "";
	private final static String iv = "";
	
	public static void main(String[] args) {

		String path = "F:\\myfile";

		
		Config.setPATH(path);
		cryptFileDirectory(path, CryptUtil.encrypt_mode);

	}
	
	public static void cryptFileDirectory(String path, String cryptMode) {
		File f = new File(path);
		if (!f.exists()){
			System.out.println("the source file is not exists!");
			return ;
		}

		File[] fs = f.listFiles();
		for (File f1 : fs) {
			System.out.println("origin source file path is :"
					+ f1.getAbsolutePath());
			String originPath = f1.getAbsolutePath();

			int index2 = originPath.lastIndexOf(File.separator);
			String fileParentPath = originPath.substring(0, index2);
			String fileName = originPath.substring(index2);

			String cryptPath = fileParentPath + File.separator + cryptMode;
			File myFilePath = new File(cryptPath);
			if (!myFilePath.exists()) {
				boolean mkdirRet = myFilePath.mkdirs();
				System.out.println("create file path is success:" + mkdirRet);
			}
			String cryptPathName = cryptPath + fileName;

			try {
				String cryptedFilePath = null;
				if (cryptMode.equals(CryptUtil.encrypt_mode)) {
					cryptedFilePath = CryptUtil.encryptFile(originPath,
							cryptPathName, key, iv);
				} else if (cryptMode.equals(CryptUtil.decrypt_mode)) {
					cryptedFilePath = CryptUtil.decryptFile(originPath,
							cryptPathName, key, iv);
				}
				if (cryptedFilePath == null){
					System.out.println("input argus wrong,please check cryptmode");
					return;
				}
				
				File cryptFile = new File(cryptedFilePath);
				String md5 = CryptUtil.checkMd5(cryptFile);
				AppLogger.w(fileName  + " md5:" + md5);
				
				System.out.println(cryptMode + " file path is:"
						+ cryptedFilePath);
			} catch (Exception e) {
				e.printStackTrace();
				File f122 = new File(cryptPathName);
				System.gc();
				boolean a = f122.delete();
				System.out.println("happen exception,delete file success:" + a);
			}
		}
	}

}
