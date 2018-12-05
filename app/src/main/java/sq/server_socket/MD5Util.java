package sq.server_socket;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

	public static String calMd5(byte[] pData) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");

		messageDigest.update(pData);
		byte[] buffer = messageDigest.digest();
		StringBuffer sb = new StringBuffer(buffer.length * 2);
		for (int i = 0; i < buffer.length; i++) {
			sb.append(Character.forDigit((buffer[i] & 240) >> 4, 16));
			sb.append(Character.forDigit(buffer[i] & 15, 16));
		}
		return sb.toString();
	}

	/**
	 * 描　述: md5对比
	 * @author bigoat
	 * 时　间: 17-1-19 上午11:11
	 * @param
	 * @return
	 */
	public static boolean md5(String path, String md5) {
		String apk = null;
		try {
			apk = getMd5ByFile(new File(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return apk.equals(md5);
	}
	public static boolean md5String(String str, String md5) {
		String apk = null;
		try {
			apk = getMd5ByFile(new File(str));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return apk.equals(md5);
	}

	public static String getMd5ByFile(File file) throws FileNotFoundException {
		String value = null;
		FileInputStream in = new FileInputStream(file);
		try {
			MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value.toUpperCase();
	}
	public static String getMd5(String string) throws NoSuchAlgorithmException {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] bs = md5.digest(string.getBytes("UTF-8"));
			StringBuilder sb = new StringBuilder(40);
			for (byte x : bs) {
				if ((x & 0xff) >> 4 == 0) {
					sb.append("0").append(Integer.toHexString(x & 0xff));
				} else {
					sb.append(Integer.toHexString(x & 0xff));
				}
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
